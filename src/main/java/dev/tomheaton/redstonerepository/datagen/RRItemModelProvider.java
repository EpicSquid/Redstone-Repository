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
        builder(itemGenerated, "string_fluxed");
        builder(itemGenerated, "rod_gelid");
        builder(itemGenerated, "gem_gelid");
        builder(itemGenerated, "dust_gelid_enderium");
        builder(itemGenerated, "gear_gelid_enderium");
        builder(itemGenerated, "nugget_gelid_enderium");
        builder(itemGenerated, "ingot_gelid_enderium");
        builder(itemGenerated, "plate_gelid_enderium");
        builder(itemGenerated, "armorplating_gelid_enderium");
        builder(itemGenerated, "coin_gelid_enderium");

        builder(itemGenerated, "gelid_enderium_helmet");
        builder(itemGenerated, "gelid_enderium_chestplate");
        builder(itemGenerated, "gelid_enderium_leggings");
        builder(itemGenerated, "gelid_enderium_boots");

        builder(itemGenerated, "axe_gelid");
        builder(itemGenerated, "battle_wrench_gelid");
        builder(itemGenerated, "bow_gelid");
        builder(itemGenerated, "excavator_gelid");
        builder(itemGenerated, "fishing_rod_gelid");
        builder(itemGenerated, "hammer_gelid");
        builder(itemGenerated, "pickaxe_gelid");
        builder(itemShield, "shield_gelid");
                /*.override()
                .predicate(new ResourceLocation(RedstoneRepository.MODID, "blocking"), 1)
                .model(builder(itemShieldBlocking, "shield_gelid_blocking"));*/
        builder(itemGenerated, "shovel_gelid");
        builder(itemGenerated, "sickle_gelid");
        builder(itemGenerated, "sword_gelid");
        builder(itemGenerated, "wrench_gelid");

        builder(itemGenerated, "capacitor_gelid");
        builder(itemGenerated, "feeder");
        builder(itemGenerated, "quiver_gelid");
        builder(itemGenerated, "ring_base");
        builder(itemGenerated, "ring_effect");
        builder(itemGenerated, "ring_mining");

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