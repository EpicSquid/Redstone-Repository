package thundr.redstonerepository.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.handlers.RegistryHandler;

import javax.annotation.Nullable;

public class RRItemTagsProvider extends ItemTagsProvider {

    public RRItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, RedstoneRepository.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Redstone Repository:
        tag(RRTags.RR_TAG)
                .add(RegistryHandler.TEST.get())
        ;

        // Curios:
        tag(RRTags.CURIOS_HEAD)
                .add(RegistryHandler.TEST_TWO.get())
        ;
        tag(RRTags.CURIOS_BELT)
                .add(RegistryHandler.TEST.get())
        ;
    }
}
