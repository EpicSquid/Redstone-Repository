package thundr.redstonerepository.items.baubles;

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
import java.lang.invoke.LambdaMetafactory;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.items.ItemCoreRF;

@Optional.Interface(iface="baubles.api.IBauble", modid="baubles")
public class ItemCapacitorAmulet extends ItemCoreRF implements IBauble, IEnergyContainerItem, IEnchantableItem, INBTCopyIngredient {
	
    @CapabilityInject(value=IBaublesItemHandler.class)
    private static Capability<IBaublesItemHandler> CAPABILITY_BAUBLES = null;

    public ItemCapacitorAmulet(int capacity, int transfer) {
        super("redstonerepository");
        this.maxEnergy = capacity;
        this.maxTransfer = transfer;
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> this.isActive(stack) ? 1.0f : 0.0f);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText((String)"info.redstonerepository.capacitor.title"));
        if (this.isActive(stack)) {
            tooltip.add(StringHelper.getDeactivationText((String)"info.redstonerepository.capacitor.deactivate"));
        } else {
            tooltip.add(StringHelper.getActivationText((String)"info.redstonerepository.capacitor.activate"));
        }
        if (!RedstoneRepositoryEquipment.EquipmentInit.enable[0]) {
            tooltip.add("\u00a74Baubles not loaded: Recipe disabled.");
        }
        tooltip.add(StringHelper.localize((String)"info.cofh.charge") + ": " + StringHelper.getScaledNumber((long)this.getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber((long)this.getCapacity(stack)) + " RF");
        tooltip.add(StringHelper.localize((String)"info.cofh.send") + "/" + StringHelper.localize((String)"info.cofh.receive") + ": " + StringHelper.formatNumber((long)this.maxTransfer) + "/" + StringHelper.formatNumber((long)this.maxTransfer) + " RF/t");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add((ItemStack)EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this, 1, 0), (int)0));
            items.add((ItemStack)EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this, 1, 0), (int)this.maxEnergy));
        }
    }

    @Optional.Method(modid="baubles")
    public void onWornTick(ItemStack cap, EntityLivingBase player) {
        if (!this.isActive(cap) || player.world.isRemote || CoreUtils.isFakePlayer((Entity)player) || !(player instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)player;
        Iterable playerItems = Iterables.concat((Iterable)entityPlayer.inventory.armorInventory, (Iterable)entityPlayer.inventory.mainInventory, (Iterable)entityPlayer.inventory.offHandInventory, ItemCapacitorAmulet.getBaubles((Entity)entityPlayer));
        for (ItemStack playerItem : playerItems) {
            IEnergyStorage handler;
            if (playerItem.isEmpty() || playerItem.equals((Object)cap) || playerItem.getItem() instanceof ItemCapacitorAmulet) continue;
            if (EnergyHelper.isEnergyContainerItem((ItemStack)playerItem)) {
                this.extractEnergy(cap, ((IEnergyContainerItem)playerItem.getItem()).receiveEnergy(playerItem, Math.min(this.getEnergyStored(cap), this.maxTransfer), false), false);
                continue;
            }
            if (!EnergyHelper.isEnergyHandler((ItemStack)playerItem) || (handler = EnergyHelper.getEnergyHandler((ItemStack)playerItem)) == null) continue;
            this.extractEnergy(cap, handler.receiveEnergy(Math.min(this.getEnergyStored(cap), this.maxTransfer), false), false);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (CoreUtils.isFakePlayer((Entity)player)) {
            return new ActionResult(EnumActionResult.FAIL, (Object)stack);
        }
        if (player.isSneaking() && this.setActiveState(stack, !this.isActive(stack))) {
            if (this.isActive(stack)) {
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.8f);
            } else {
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.5f);
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, (Object)stack);
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
        int enchant = EnchantmentHelper.getEnchantmentLevel((Enchantment)CoreEnchantments.holding, (ItemStack)stack);
        return this.maxEnergy + this.maxEnergy * enchant / 2;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return this.getCapacity(stack);
    }

    private static Iterable<ItemStack> getBaubles(Entity entity) {
        if (CAPABILITY_BAUBLES == null) {
            return Collections.emptyList();
        }
        IBaublesItemHandler handler = (IBaublesItemHandler)entity.getCapability(CAPABILITY_BAUBLES, null);
        if (handler == null) {
            return Collections.emptyList();
        }
        return IntStream.range(0, handler.getSlots()).mapToObj((IntFunction<ItemStack>)LambdaMetafactory.metafactory(null, null, null, (I)Ljava/lang/Object;, getStackInSlot(int ), (I)Lnet/minecraft/item/ItemStack;)((IBaublesItemHandler)handler)).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    @Optional.Method(modid="baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag((ItemStack)stack, (int)0);
        }
        return 1.0 - (double)stack.getTagCompound().getInteger("Energy") / (double)this.getCapacity(stack);
    }

    public boolean setMode(ItemStack stack, int mode) {
        return false;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}

