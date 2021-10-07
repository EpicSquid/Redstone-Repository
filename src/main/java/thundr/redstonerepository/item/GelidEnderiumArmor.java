package thundr.redstonerepository.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import thundr.redstonerepository.RedstoneRepository;

public class GelidEnderiumArmor extends ArmorItem {

    public GelidEnderiumArmor(EquipmentSlotType slot) {
        super(RRArmorMaterial.GELID_ENDERIUM, slot, new Properties().tab(RedstoneRepository.tabRedstoneRepository));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }
}
