package thundr.redstonerepository.item.armor;

import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstonearsenal.item.armor.ItemArmorFlux;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.api.IArmorEnderium;

import javax.annotation.Nullable;
import java.util.List;

public class ItemArmorGelid extends ItemArmorFlux implements IArmorEnderium {

    public ItemArmorGelid(ItemArmor.ArmorMaterial material, EntityEquipmentSlot type) {
        super(material, type);
        this.setNoRepair();
        this.setCreativeTab(RedstoneRepository.tabCommon);
        this.setMaxDamage(5);
        this.maxEnergy = 4000000;
        this.energyPerDamage = 4500;
        this.absorbRatio = 0.95;
        this.maxTransfer = 20000;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem().getRegistryName().toString().contains("boot")) {
            tooltip.add("\u00a78Falling costs energy, but does no damage.");
        }
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        tooltip.add("\u00a7bFull Set: \u00a78Fire Damage ineffective, but drains energy. ");
        tooltip.add(StringHelper.localize("info.cofh.charge") + ": " + StringHelper.formatNumber(stack.getTagCompound().getInteger("Energy")) + " / " + StringHelper.formatNumber(this.getMaxEnergyStored(stack)) + " RF");
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }

    @Override
    public boolean isEnderiumArmor(ItemStack stack) {
        return true;
    }
}

