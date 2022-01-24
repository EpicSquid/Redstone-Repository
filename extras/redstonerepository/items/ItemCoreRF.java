package tomheaton.redstonerepository.item;

import cofh.api.item.IMultiModeItem;
import cofh.core.init.CoreEnchantments;
import cofh.core.item.IEnchantableItem;
import cofh.core.item.ItemCore;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.MathHelper;
import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.util.EnergyContainerItemWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemCoreRF extends ItemCore implements IMultiModeItem, IEnergyContainerItem, IEnchantableItem {

    protected int maxEnergy = 32000;
    protected int maxTransfer = 1000;
    protected int energyPerUse = 200;
    protected boolean isCreative = false;
    protected boolean showInCreative = true;

    public ItemCoreRF(String modName) {
        super(modName);
    }

    public ItemCoreRF setEnergyParams(int maxEnergy, int maxTransfer, int energyPerUse) {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.energyPerUse = energyPerUse;
        return this;
    }

    public ItemCoreRF setCreative(boolean creative) {
        this.isCreative = creative;
        return this;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab) && this.showInCreative) {
            if (!this.isCreative) {
                items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), 0));
            }
            items.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(this, 1, 0), this.maxEnergy));
        }
    }

    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    public boolean isDamageable() {
        return false;
    }

    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public boolean isFull3D() {
        return true;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || this.getEnergyStored(oldStack) > 0 != this.getEnergyStored(newStack) > 0);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return this.getEnergyStored(stack) > 0;
    }

    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 13635600;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        return 1.0 - (double) stack.getTagCompound().getInteger("Energy") / (double) this.getMaxEnergyStored(stack);
    }

    protected int getEnergyPerUse(ItemStack stack) {
        if (this.isCreative) {
            return 0;
        }
        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 4);
        return this.energyPerUse * (5 - unbreakingLevel) / 5;
    }

    protected int getTransfer(ItemStack container) {
        return this.maxTransfer;
    }

    protected int useEnergy(ItemStack stack, int count, boolean simulate) {
        if (this.isCreative) {
            return 0;
        }
        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 4);
        return this.extractEnergy(stack, count * this.energyPerUse * (5 - unbreakingLevel) / 5, simulate);
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = Math.min(container.getTagCompound().getInteger("Energy"), this.getMaxEnergyStored(container));
        int receive = Math.min(maxReceive, Math.min(this.getMaxEnergyStored(container) - stored, this.getTransfer(container)));
        if (!simulate && !this.isCreative) {
            container.getTagCompound().setInteger("Energy", stored += receive);
        }
        return receive;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        if (this.isCreative) {
            return maxExtract;
        }
        int stored = Math.min(container.getTagCompound().getInteger("Energy"), this.getMaxEnergyStored(container));
        int extract = Math.min(maxExtract, stored);
        if (!simulate) {
            container.getTagCompound().setInteger("Energy", stored -= extract);
        }
        return extract;
    }

    public int getEnergyStored(ItemStack container) {
        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        return Math.min(container.getTagCompound().getInteger("Energy"), this.getMaxEnergyStored(container));
    }

    public int getMaxEnergyStored(ItemStack container) {
        int enchant = EnchantmentHelper.getEnchantmentLevel(CoreEnchantments.holding, container);
        return this.maxEnergy + this.maxEnergy * enchant / 2;
    }

    public boolean canEnchant(ItemStack stack, Enchantment enchantment) {
        return enchantment == CoreEnchantments.holding;
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new EnergyContainerItemWrapper(stack, this);
    }
}

