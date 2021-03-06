package thundr.redstonerepository.init;

import cofh.core.util.core.IInitializer;
import java.util.ArrayList;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thundr.redstonerepository.blocks.BlockStorage;

public class RedstoneRepositoryBlocks {
    public static final RedstoneRepositoryBlocks INSTANCE = new RedstoneRepositoryBlocks();
    static ArrayList<IInitializer> initList = new ArrayList();
    public static BlockStorage BlockStorage;

    private RedstoneRepositoryBlocks() {
    }

    public static void preInit() {
        BlockStorage = new BlockStorage();
        initList.add(BlockStorage);
        for (IInitializer init : initList) {
            init.preInit();
        }
        MinecraftForge.EVENT_BUS.register((Object)INSTANCE);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (IInitializer init : initList) {
            init.initialize();
        }
    }
}

