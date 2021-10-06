package thundr.redstonerepository.item.util;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import cofh.api.item.INBTCopyIngredient;
import cofh.core.init.CoreEnchantments;
import cofh.core.item.IEnchantableItem;
import cofh.core.util.CoreUtils;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstoneflux.api.IEnergyContainerItem;
import com.google.common.collect.Iterables;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.item.ItemCoreRF;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemCapacitorAmulet extends ItemCoreRF implements IBauble, IEnergyContainerItem, IEnchantableItem, INBTCopyIngredient {

    protected int maxEnergy = 320000;
    protected int maxTransfer = 4000;
    protected int energyPerUse = 800;
    protected int energyPerUseCharged = 6400;

    @CapabilityInject(value = IBaublesItemHandler.class)
    private static final Capability<IBaublesItemHandler> CAPABILITY_BAUBLES = null;

    public ItemCapacitorAmulet() {
        super(RedstoneRepository.MODID);
        setMaxDamage(0);
        setNoRepair();
        setMaxStackSize(1);
        setUnlocalizedName("redstonerepository.util.gelidCapacitor");
        setCreativeTab(RedstoneRepository.tabCommon);
        addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> this.isActive(stack) ? 1.0f : 0.0f);
    }

    public ItemCapacitorAmulet(int capacity, int transfer) {
        super(RedstoneRepository.MODID);
        this.maxEnergy = capacity;
        this.maxTransfer = transfer;
        setUnlocalizedName("redstonerepository.util.gelidCapacitor");
        setCreativeTab(RedstoneRepository.tabCommon);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setNoRepair();
        addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> this.isActive(stack) ? 1.0f : 0.0f);
    }

    private static Iterable<ItemStack> getBaubles(Entity entity) {
        if (CAPABILITY_BAUBLES == null) {
            return Collections.emptyList();
        } else {
            IBaublesItemHandler handler = entity.getCapability(CAPABILITY_BAUBLES, null);
            if (handler == null) {
                return Collections.emptyList();
            } else {
                IntStream stream = IntStream.range(0, handler.getSlots());
                handler.getClass();
                return stream.mapToObj(handler::getStackInSlot).filter((stack) -> !stack.isEmpty()).collect(Collectors.toList());
            }
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.redstonerepository.capacitor.title"));
        if (this.isActive(stack)) {
            tooltip.add(StringHelper.getDeactivationText("info.redstonerepository.capacitor.deactivate"));
        } else {
            tooltip.add(StringHelper.getActivationText("info.redstonerepository.capacitor.activate"));
        }
        if (!RedstoneRepositoryEquipment.EquipmentInit.enable[0]) {
            tooltip.add("\u00a74Baubles not loaded: Recipe disabled.");
        }
        tooltip.add(StringHelper.localize("info.cofh.charge") + ": " + StringHelper.getScaledNumber(this.getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber(this.getCapacity(stack)) + " RF");
        tooltip.add(StringHelper.localize("info.cofh.send") + "/" + StringHelper.localize("info.cofh.receive") + ": " + StringHelper.formatNumber(this.maxTransfer) + "/" + StringHelper.formatNumber(this.maxTransfer) + " RF/t");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0));
            items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), this.maxEnergy));
        }
    }

    //@Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack cap, EntityLivingBase player) {
        if (!this.isActive(cap) || player.world.isRemote || CoreUtils.isFakePlayer(player) || !(player instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer) player;
        Iterable<ItemStack> playerItems = Iterables.concat(entityPlayer.inventory.armorInventory, entityPlayer.inventory.mainInventory, entityPlayer.inventory.offHandInventory, ItemCapacitorAmulet.getBaubles(entityPlayer));
        for (ItemStack playerItem : playerItems) {
            IEnergyStorage handler;
            if (playerItem.isEmpty() || playerItem.equals(cap) || playerItem.getItem() instanceof ItemCapacitorAmulet)
                continue;
            if (EnergyHelper.isEnergyContainerItem(playerItem)) {
                this.extractEnergy(cap, ((IEnergyContainerItem) playerItem.getItem()).receiveEnergy(playerItem, Math.min(this.getEnergyStored(cap), this.maxTransfer), false), false);
                continue;
            }
            if (!EnergyHelper.isEnergyHandler(playerItem) || (handler = EnergyHelper.getEnergyHandler(playerItem)) == null)
                continue;
            this.extractEnergy(cap, handler.receiveEnergy(Math.min(this.getEnergyStored(cap), this.maxTransfer), false), false);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (CoreUtils.isFakePlayer(player)) {
            return new ActionResult(EnumActionResult.FAIL, stack);
        }
        if (player.isSneaking() && this.setActiveState(stack, !this.isActive(stack))) {
            if (this.isActive(stack)) {
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.8f);
            } else {
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.5f);
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.FAIL;
    }

    public boolean isActive(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("Active");
    }

    public boolean setActiveState(ItemStack stack, boolean state) {
        if (this.getEnergyStored(stack) > 0) {
            stack.getTagCompound().setBoolean("Active", state);
            return true;
        }
        stack.getTagCompound().setBoolean("Active", false);
        return false;
    }

    protected int getCapacity(ItemStack stack) {
        int enchant = EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, stack);
        return this.maxEnergy + this.maxEnergy * enchant / 2;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return this.getCapacity(stack);
    }

    //@Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        return 1.0 - (double) stack.getTagCompound().getInteger("Energy") / (double) this.getCapacity(stack);
    }

    public boolean setMode(ItemStack stack, int mode) {
        return false;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}

