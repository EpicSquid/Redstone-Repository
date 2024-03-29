package thundr.redstonerepository.item.util;

import cofh.api.item.IMultiModeItem;
import cofh.api.item.IToolQuiver;
import cofh.core.init.CoreEnchantments;
import cofh.core.init.CoreProps;
import cofh.core.item.IEnchantableItem;
import cofh.core.item.ItemCore;
import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstonearsenal.init.RAProps;
import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.util.EnergyContainerItemWrapper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.entity.projectile.EntityArrowGelid;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.core.util.helpers.RecipeHelper.addShapedRecipe;

// TODO: rework this because it is hard-coded. (Credit to King Lemming)
public class ItemQuiverGelid extends ItemCore implements IInitializer, IModelRegister, IEnchantableItem, IEnergyContainerItem, IMultiModeItem, IToolQuiver {

    public static ItemStack quiverGelidEnderium;

    protected int maxEnergy = 320000;
    protected int maxTransfer = 4000;
    protected int energyPerUse = 800;
    protected int energyPerUseCharged = 6400;

    protected boolean showInCreative = true;
    public static boolean enable;

    public ItemQuiverGelid() {
        super(RedstoneRepository.MODID);
        setMaxDamage(0);
        setNoRepair();
        setMaxStackSize(1);
        setUnlocalizedName("redstonerepository.util.gelidQuiver");
        setCreativeTab(RedstoneRepository.tabCommon);
        addPropertyOverride(new ResourceLocation("active"), (stack, world, entity) -> ItemQuiverGelid.this.getEnergyStored(stack) > 0 && !ItemQuiverGelid.this.isEmpowered(stack) ? 1F : 0F);
        addPropertyOverride(new ResourceLocation("empowered"), (stack, world, entity) -> ItemQuiverGelid.this.isEmpowered(stack) ? 1F : 0F);
    }

    public ItemQuiverGelid setEnergyParams(int maxEnergy, int maxTransfer, int energyPerUse, int energyPerUseCharged) {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.energyPerUse = energyPerUse;
        this.energyPerUseCharged = energyPerUseCharged;
        return this;
    }

    protected boolean isEmpowered(ItemStack stack) {
        return getMode(stack) == 1 && getEnergyStored(stack) >= energyPerUseCharged;
    }

    protected int getEnergyPerUse(ItemStack stack) {
        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 4);
        return (isEmpowered(stack) ? energyPerUseCharged : energyPerUse) * (5 - unbreakingLevel) / 5;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        tooltip.add(StringHelper.localize("info.cofh.charge") + ": " + StringHelper.getScaledNumber(getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber(getMaxEnergyStored(stack)) + " RF");
        tooltip.add(StringHelper.ORANGE + getEnergyPerUse(stack) + " " + StringHelper.localize("info.redstonearsenal.tool.energyPerUse") + StringHelper.END);
        RAProps.addEmpoweredTip(this, stack, tooltip);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab) && showInCreative) {
            items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0));
            items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), maxEnergy));
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (EnumEnchantmentType.BREAKABLE.equals(enchantment.type)) {
            return enchantment.equals(Enchantments.UNBREAKING);
        }
        return enchantment.type.canEnchantItem(this);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || getEnergyStored(oldStack) > 0 != getEnergyStored(newStack) > 0);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return RAProps.showToolCharge && getEnergyStored(stack) > 0;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 10;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        return MathHelper.clamp(1.0D - ((double) stack.getTagCompound().getInteger(CoreProps.ENERGY) / (double) getMaxEnergyStored(stack)), 0.0D, 1.0D);
    }

    @Override
    public boolean canEnchant(ItemStack stack, Enchantment enchantment) {
        return enchantment == CoreEnchantments.holding;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = Math.min(container.getTagCompound().getInteger(CoreProps.ENERGY), getMaxEnergyStored(container));
        int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, maxTransfer));
        if (!simulate) {
            stored += receive;
            container.getTagCompound().setInteger(CoreProps.ENERGY, stored);
        }
        return receive;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = Math.min(container.getTagCompound().getInteger(CoreProps.ENERGY), getMaxEnergyStored(container));
        int extract = Math.min(maxExtract, stored);
        if (!simulate) {
            stored -= extract;
            container.getTagCompound().setInteger(CoreProps.ENERGY, stored);
            if (stored == 0) {
                setMode(container, 0);
            }
        }
        return extract;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        return Math.min(container.getTagCompound().getInteger(CoreProps.ENERGY), getMaxEnergyStored(container));
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        int enchant = EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, container);
        return maxEnergy + maxEnergy * enchant / 2;
    }

    @Override
    public EntityArrow createEntityArrow(World world, ItemStack item, EntityLivingBase shooter) {
        return new EntityArrowGelid(world, shooter, isEmpowered(item));
    }

    @Override
    public boolean allowCustomArrowOverride(ItemStack item) {
        return false;
    }

    @Override
    public boolean isEmpty(ItemStack item, EntityLivingBase shooter) {
        return !(shooter instanceof EntityPlayer && ((EntityPlayer) shooter).capabilities.isCreativeMode) && getEnergyStored(item) <= 0;
    }

    @Override
    public void onArrowFired(ItemStack item, EntityLivingBase shooter) {
        if (shooter instanceof EntityPlayer) {
            extractEnergy(item, getEnergyPerUse(item), ((EntityPlayer) shooter).capabilities.isCreativeMode);
        }
    }

    @Override
    public void onModeChange(EntityPlayer player, ItemStack stack) {
        if (isEmpowered(stack)) {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 0.4F, 1.0F);
        } else {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.6F);
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new EnergyContainerItemWrapper(stack, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(RedstoneRepository.MODID, "util/quiver_gelid"), "inventory"));
    }

    @Override
    public boolean preInit() {
        this.setRegistryName("util.quiver_gelid");
        ForgeRegistries.ITEMS.register(this);
        config();
        this.showInCreative = enable;
        quiverGelidEnderium = EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0);
        RedstoneRepository.PROXY.addIModelRegister(this);
        return true;
    }

    @Override
    public boolean initialize() {
        if (!enable) {
            return false;
        }
        addShapedRecipe(quiverGelidEnderium, "AA ", "GIS", "IGS", 'A', Items.ARROW, 'G', "gemGelidCrystal", 'I', "ingotGelidEnderium", 'S', "stringFluxed");
        return true;
    }

    private static void config() {
        String category = "Equipment.Tools.Gelid";
        enable = RedstoneRepository.CONFIG_COMMON.get(category, "Quiver", true);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}