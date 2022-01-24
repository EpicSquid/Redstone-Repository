package dev.tomheaton.redstonerepository.datagen;

import dev.tomheaton.redstonerepository.RedstoneRepository;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RRItemModelProvider extends ItemModelProvider {

    public RRItemModelProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, RedstoneRepository.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        ModelFile itemShield = getExistingFile(mcLoc("item/shield"));
        ModelFile itemShieldBlocking = getExistingFile(mcLoc("item/shield_blocking"));

        // Redstone Repository:
        builder(itemGenerated, "gelid_coin");
        builder(itemGenerated, "gelid_dust");
        builder(itemGenerated, "gelid_gear");
        builder(itemGenerated, "gelid_gem");
        builder(itemGenerated, "gelid_ingot");
        builder(itemGenerated, "gelid_nugget");
        builder(itemGenerated, "gelid_plate");
        builder(itemGenerated, "gelid_plating");
        builder(itemGenerated, "flux_string");
        builder(itemGenerated, "gelid_obsidian_rod");

        builder(itemGenerated, "gelid_helmet");
        builder(itemGenerated, "gelid_chestplate");
        builder(itemGenerated, "gelid_leggings");
        builder(itemGenerated, "gelid_boots");
        builder(itemGenerated, "gelid_elytra");
        builder(itemGenerated, "gelid_elytra_controller");

        builder(itemGenerated, "gelid_sword");
        builder(itemShield, "gelid_shield");
                /*.override()
                .predicate(new ResourceLocation(RedstoneRepository.MODID, "blocking"), 1)
                .model(builder(itemShieldBlocking, "gelid_shield_blocking"));*/
        builder(itemGenerated, "gelid_trident");
        builder(itemGenerated, "gelid_bow");
        builder(itemGenerated, "gelid_crossbow");
        builder(itemGenerated, "gelid_quiver");

        builder(itemGenerated, "gelid_pickaxe");
        builder(itemGenerated, "gelid_shovel");
        builder(itemGenerated, "gelid_axe");
        builder(itemGenerated, "gelid_hoe");
        builder(itemGenerated, "gelid_sickle");
        builder(itemGenerated, "gelid_hammer");
        builder(itemGenerated, "gelid_excavator");
        builder(itemGenerated, "gelid_fishing_rod");
        builder(itemGenerated, "gelid_wrench");

        builder(itemGenerated, "gelid_capacitor");
        builder(itemGenerated, "gelid_feeder");
        builder(itemGenerated, "gelid_ring_base");
        builder(itemGenerated, "gelid_ring_effect");
        builder(itemGenerated, "gelid_ring_mining");

        // Patchouli:
        builder(itemGenerated, "guidebook");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }

/*    private ItemModelBuilder builderShield(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name)
                .override(new ResourceLocation(RedstoneRepository.MODID, "item/")).predicate().model();
    }*/

}
