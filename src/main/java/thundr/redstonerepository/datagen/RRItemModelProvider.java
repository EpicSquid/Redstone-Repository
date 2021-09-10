package thundr.redstonerepository.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import thundr.redstonerepository.RedstoneRepository;

public class RRItemModelProvider extends ItemModelProvider {

    public RRItemModelProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RedstoneRepository.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        // Redstone Repository:
        builder(itemGenerated, "string_fluxed");
        builder(itemGenerated, "rod_gelid");
        builder(itemGenerated, "gem_gelid");
        builder(itemGenerated, "dust_gelid_enderium");
        builder(itemGenerated, "gear_gelid_enderium");
        builder(itemGenerated, "nugget_gelid_enderium");
        builder(itemGenerated, "ingot_gelid_enderium");
        builder(itemGenerated, "plate_gelid_enderium");
        builder(itemGenerated, "armorplating_gelid_enderium");

        builder(itemGenerated, "gelid_enderium_helmet");
        builder(itemGenerated, "gelid_enderium_chestplate");
        builder(itemGenerated, "gelid_enderium_leggings");
        builder(itemGenerated, "gelid_enderium_boots");

        // Patchouli:
        builder(itemGenerated, "guidebook");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
