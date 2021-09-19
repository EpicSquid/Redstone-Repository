package thundr.redstonerepository.item.util;

import cofh.redstonearsenal.item.util.ItemQuiverFlux;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import thundr.redstonerepository.RedstoneRepository;

import static cofh.core.util.helpers.RecipeHelper.addShapedRecipe;

public class ItemQuiverGelidOld extends ItemQuiverFlux {

    public ItemQuiverGelidOld() {
        super();
    }

    @Override
    public boolean initialize() {
        if (!enable) {
            return false;
        }
        addShapedRecipe(quiverElectrumFlux, "AA ", "GIS", "IGS", 'A', Items.ARROW, 'G', "gemGelidCrystal", 'I', "ingotGelidEnderium", 'S', Items.STRING);
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
