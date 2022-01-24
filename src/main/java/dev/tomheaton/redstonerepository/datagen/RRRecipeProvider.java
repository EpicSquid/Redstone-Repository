package dev.tomheaton.redstonerepository.datagen;

import com.google.gson.JsonObject;
import dev.tomheaton.redstonerepository.RedstoneRepository;
import dev.tomheaton.redstonerepository.handlers.RegistryHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RRRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public RRRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {

        // Testing:
        CustomShapedRecipeBuilder.shaped(RegistryHandler.FLUX_STRING.get())
                .pattern("S S")
                .pattern("GPG")
                .pattern("R R")
                .define('S', forgeTag("string"))
                .define('G', forgeTag("nuggets/gold"))
                .define('P', forgeTag("crops/potato"))
                .define('R', forgeTag("dusts/redstone"))
                .save(consumer);

        CustomShapelessRecipeBuilder.shapeless(RegistryHandler.GELID_OBSIDIAN_ROD.get(), 2)
                .requires(Items.COAL)
                .requires(Items.CLAY_BALL)
                .requires(Items.GUNPOWDER)
                .requires(Items.BONE_MEAL)
                .save(consumer);

    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(RedstoneRepository.MODID, path);
    }

    private static Tags.IOptionalNamedTag<Item> forgeTag(String name) {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }

    private static Ingredient fromJson(String name) {
        JsonObject object = new JsonObject();
        object.addProperty("item", name);
        return Ingredient.fromJson(object);
    }

    private static String savePath(String path) {
        return new ResourceLocation(RedstoneRepository.MODID, path).toString();
    }
}
