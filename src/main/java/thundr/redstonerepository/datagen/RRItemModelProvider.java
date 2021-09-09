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

        // Patchouli:
        builder(itemGenerated, "guidebook");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
