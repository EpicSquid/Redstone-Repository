package thundr.redstonerepository.init;

import cofh.core.util.core.IInitializer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thundr.redstonerepository.item.ItemMaterial;
import thundr.redstonerepository.item.util.ItemCapacitorAmulet;
import thundr.redstonerepository.item.util.ItemFeeder;
import thundr.redstonerepository.item.util.ItemQuiverGelid;

import java.util.ArrayList;

public class RedstoneRepositoryItems {

    public static final RedstoneRepositoryItems INSTANCE = new RedstoneRepositoryItems();
    public static ItemMaterial itemMaterial;
    public static ItemQuiverGelid itemQuiverGelid;
    public static ItemFeeder itemFeeder;
    public static ItemCapacitorAmulet itemCapacitorGelid;
    static ArrayList<IInitializer> initList = new ArrayList<>();

    private RedstoneRepositoryItems() {
    }

    public static void preInit() {
        itemMaterial = new ItemMaterial();
        itemQuiverGelid = new ItemQuiverGelid();
        itemFeeder = new ItemFeeder();
        itemCapacitorGelid = new ItemCapacitorAmulet();

        initList.add(itemMaterial);
        initList.add(itemQuiverGelid);
        initList.add(itemFeeder);
        initList.add(itemCapacitorGelid);

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

