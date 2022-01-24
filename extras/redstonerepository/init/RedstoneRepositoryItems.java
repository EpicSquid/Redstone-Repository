package tomheaton.redstonerepository.init;

import cofh.core.util.core.IInitializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tomheaton.redstonerepository.item.ItemMaterial;

import java.util.ArrayList;

public class RedstoneRepositoryItems {

    public static final RedstoneRepositoryItems INSTANCE = new RedstoneRepositoryItems();
    public static ItemMaterial itemMaterial;
    static ArrayList<IInitializer> initList = new ArrayList<>();

    private RedstoneRepositoryItems() {
    }

    public static void preInit() {
        itemMaterial = new ItemMaterial();
        initList.add(itemMaterial);
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

