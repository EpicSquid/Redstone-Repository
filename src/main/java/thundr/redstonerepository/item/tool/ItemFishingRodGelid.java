package thundr.redstonerepository.item.tool;

import cofh.redstonearsenal.item.tool.ItemFishingRodFlux;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemFishingRodGelid extends ItemFishingRodFlux {

    public ItemFishingRodGelid(ToolMaterial toolMaterial) {
        super(toolMaterial);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}
