package thundr.redstonerepository.items.armor;

import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstonearsenal.item.armor.ItemArmorFlux;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.api.IArmorEnderium;

public class ItemArmorEnderium
extends ItemArmorFlux
implements IArmorEnderium {
    public ItemArmorEnderium(ItemArmor.ArmorMaterial material, EntityEquipmentSlot type) {
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
            EnergyHelper.setDefaultEnergyTag((ItemStack)stack, (int)0);
        }
        tooltip.add("\u00a7bFull Set: \u00a78Fire Damage ineffective, but drains energy. ");
        tooltip.add(StringHelper.localize((String)"info.cofh.charge") + ": " + StringHelper.formatNumber((long)stack.getTagCompound().getInteger("Energy")) + " / " + StringHelper.formatNumber((long)this.getMaxEnergyStored(stack)) + " RF");
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

