package dev.tomheaton.redstonerepository.datagen;

import dev.tomheaton.redstonerepository.RedstoneRepository;
import dev.tomheaton.redstonerepository.handlers.RegistryHandler;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class RRItemTagsProvider extends ItemTagsProvider {

    public RRItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, RedstoneRepository.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Redstone Repository:
        tag(RRTags.RR_TAG)
                .add(RegistryHandler.FLUX_STRING.get())
        ;

        // Curios:
        tag(RRTags.CURIOS_BELT)
                .add(RegistryHandler.GELID_FEEDER.get())
        ;
        tag(RRTags.CURIOS_RING)
                .add(RegistryHandler.RING_BASE.get())
                .add(RegistryHandler.RING_EFFECT.get())
                .add(RegistryHandler.RING_MINING.get())
        ;
        tag(RRTags.CURIOS_NECKLACE)
                .add(RegistryHandler.CAPACITOR_GELID.get())
        ;
    }
}
