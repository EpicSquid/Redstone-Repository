package thundr.redstonerepository.item.tool;

import cofh.redstonearsenal.item.tool.ItemExcavatorFlux;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemExcavatorGelid extends ItemExcavatorFlux {

    public ItemExcavatorGelid(ToolMaterial toolMaterial) {
        super(toolMaterial);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}
