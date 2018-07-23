package thundr.redstonerepository.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.api.item.IInventoryContainerItem;
import cofh.core.init.CoreEnchantments;
import cofh.core.key.KeyBindingItemMultiMode;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.StringHelper;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.api.IHungerStorageItem;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.items.ItemCoreRF;
import thundr.redstonerepository.util.HungerHelper;

@Optional.Interface(iface="baubles.api.IBauble", modid="baubles")
public class ItemFeeder extends ItemCoreRF implements IBauble, IInventoryContainerItem, IHungerStorageItem {
	
    public int hungerPointsMax;
    private int saturationFillMax;

    public ItemFeeder() {
        super("Redstone Repository");
        this.setMaxStackSize(1);
        this.setCreativeTab(RedstoneRepository.tabCommon);
    }

    public ItemFeeder(int hungerPointsMax, int maxEnergy, int maxTransfer, int energyPerUse, int saturationFillMax) {
        super("Redstone Repository");
        this.setMaxStackSize(1);
        this.setCreativeTab(RedstoneRepository.tabCommon);
        this.setNoRepair();
        this.hungerPointsMax = hungerPointsMax;
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.energyPerUse = energyPerUse;
        this.saturationFillMax = saturationFillMax;
        this.addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> this.getMode(stack) == MODE.ENABLED.getValue() ? 1.0f : 0.0f);
    }

    @Optional.Method(modid="baubles")
    public void onWornTick(ItemStack feeder, EntityLivingBase player) {
        if (player.isServerWorld() && this.getMode(feeder) == MODE.ENABLED.getValue() && this.getHungerPoints(feeder) > 0 && this.getEnergyStored(feeder) >= this.getEnergyPerUse(feeder) && player instanceof EntityPlayer) {
            EntityPlayer ePlayer = (EntityPlayer)player;
            if (ePlayer.getFoodStats().needFood()) {
                HungerHelper.addHunger(ePlayer, 1);
                this.useHungerPoints(feeder, 1, ePlayer);
                this.useEnergy(feeder, 1, false);
            } else if (ePlayer.getFoodStats().getSaturationLevel() < (float)this.saturationFillMax) {
                HungerHelper.addSaturation(ePlayer, 1);
                this.useHungerPoints(feeder, 1, ePlayer);
                this.useEnergy(feeder, 1, false);
            }
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.getInfoText((String)"info.redstonerepository.feeder.short"));
            return;
        }
        tooltip.add(StringHelper.getInfoText((String)"info.redstonerepository.feeder.title"));
        if (this.getMode(stack) == MODE.ENABLED.getValue()) {
            tooltip.add(StringHelper.localizeFormat((String)"info.redstonerepository.feeder.active", (Object[])new Object[]{"\u00a7a", "\u00a7r", StringHelper.getKeyName((int)KeyBindingItemMultiMode.INSTANCE.getKey())}));
        } else {
            tooltip.add(StringHelper.localizeFormat((String)"info.redstonerepository.feeder.disabled", (Object[])new Object[]{"\u00a7c", "\u00a7r", StringHelper.getKeyName((int)KeyBindingItemMultiMode.INSTANCE.getKey())}));
        }
        if (!RedstoneRepositoryEquipment.EquipmentInit.enable[1]) {
            tooltip.add("\u00a74Baubles not loaded: Recipe disabled.");
        }
        tooltip.add(StringHelper.localize((String)"info.redstonerepository.hungerPoints") + ": " + "\u00a76" + StringHelper.getScaledNumber((long)this.getHungerPoints(stack)) + " / " + StringHelper.getScaledNumber((long)this.getMaxHungerPoints(stack)));
        tooltip.add(StringHelper.localize((String)"info.cofh.charge") + ": " + "\u00a74" + StringHelper.getScaledNumber((long)this.getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber((long)this.getCapacity(stack)) + " RF");
    }

    protected int getCapacity(ItemStack stack) {
        int enchant = EnchantmentHelper.getEnchantmentLevel((Enchantment)CoreEnchantments.holding, (ItemStack)stack);
        return this.maxEnergy + this.maxEnergy * enchant / 2;
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return this.getCapacity(stack);
    }

    public void onModeChange(EntityPlayer player, ItemStack stack) {
        if (this.getMode(stack) == MODE.ENABLED.getValue()) {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.8f);
        } else {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2f, 0.5f);
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add((ItemStack)EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this, 1, 0), (int)0));
            items.add((ItemStack)EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this, 1, 0), (int)this.maxEnergy));
        }
    }

    @Optional.Method(modid="baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BELT;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        player.openGui((Object)RedstoneRepository.instance, 0, world, 0, 0, 0);
        return new ActionResult(EnumActionResult.SUCCESS, (Object)stack);
    }

    public int getSizeInventory(ItemStack container) {
        return 1;
    }

    @Override
    public int getHungerPoints(ItemStack container) {
        HungerHelper.setDefaultHungerTag(container);
        return Math.min(container.getTagCompound().getInteger("Hunger"), this.getMaxHungerPoints(container));
    }

    @Override
    public int receiveHungerPoints(ItemStack container, int maxReceive, boolean simulate) {
        HungerHelper.setDefaultHungerTag(container);
        int stored = Math.min(container.getTagCompound().getInteger("Hunger"), this.getMaxHungerPoints(container));
        int receive = Math.min(maxReceive, this.getMaxHungerPoints(container) - stored);
        if (!this.isCreative && !simulate) {
            container.getTagCompound().setInteger("Hunger", stored += receive);
        }
        return receive;
    }

    @Override
    public int useHungerPoints(ItemStack container, int maxExtract, EntityPlayer player) {
        if (this.isCreative) {
            return maxExtract;
        }
        HungerHelper.setDefaultHungerTag(container);
        int stored = Math.min(container.getTagCompound().getInteger("Hunger"), this.getMaxHungerPoints(container));
        int extract = Math.min(maxExtract, stored);
        container.getTagCompound().setInteger("Hunger", stored -= extract);
        return extract;
    }

    @Override
    public int getMaxHungerPoints(ItemStack container) {
        int enchant = EnchantmentHelper.getEnchantmentLevel((Enchantment)CoreEnchantments.holding, (ItemStack)container);
        return this.hungerPointsMax + this.hungerPointsMax * enchant / 2;
    }

    public static enum MODE {
        DISABLED(0),
        ENABLED(1);
        
        private final int value;

        private MODE(int newValue) {
            this.value = newValue;
        }

        public int getValue() {
            return this.value;
        }
    }

}

