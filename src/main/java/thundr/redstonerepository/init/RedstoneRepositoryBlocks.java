package thundr.redstonerepository.init;

import cofh.core.util.core.IInitializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thundr.redstonerepository.blocks.BlockStorage;

import java.util.ArrayList;

public class RedstoneRepositoryBlocks {
    public static final RedstoneRepositoryBlocks INSTANCE = new RedstoneRepositoryBlocks();
    public static BlockStorage BlockStorage;
    static ArrayList<IInitializer> initList = new ArrayList();

    private RedstoneRepositoryBlocks() {
    }

    public static void preInit() {
        BlockStorage = new BlockStorage();
        initList.add(BlockStorage);
        for (IInitializer init : initList) {
            init.preInit();
        }
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (IInitializer init : initList) {
            init.initialize();
        }
    }
}

