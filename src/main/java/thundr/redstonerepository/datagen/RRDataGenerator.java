package thundr.redstonerepository.datagen;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import thundr.redstonerepository.RedstoneRepository;

@Mod.EventBusSubscriber(modid = RedstoneRepository.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RRDataGenerator {

    private RRDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(new RRItemModelProvider(gen, existingFileHelper));
        gen.addProvider(new RRItemTagsProvider(gen, new BlockTagsProvider(gen, RedstoneRepository.MODID, existingFileHelper), existingFileHelper));
        gen.addProvider(new RRRecipeProvider(gen));
        gen.addProvider(new RRAdvancementProvider(gen));
    }
}
