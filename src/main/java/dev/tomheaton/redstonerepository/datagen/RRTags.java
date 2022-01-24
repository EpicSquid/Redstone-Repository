package dev.tomheaton.redstonerepository.datagen;

import dev.tomheaton.redstonerepository.RedstoneRepository;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class RRTags {

    public static final ITag.INamedTag<Item> RR_TAG = mod("redstone_repository_tag");

    public static final ITag.INamedTag<Item> CURIOS_BELT = curios("belt");
    public static final ITag.INamedTag<Item> CURIOS_RING = curios("ring");
    public static final ITag.INamedTag<Item> CURIOS_NECKLACE = curios("necklace");

    private static ITag.INamedTag<Item> forge(String path) {
        return ItemTags.createOptional(new ResourceLocation("forge", path));
    }
    private static ITag.INamedTag<Item> mod(String path) {
        return ItemTags.createOptional(new ResourceLocation(RedstoneRepository.MODID, path));
    }
    private static ITag.INamedTag<Item> curios(String path) {
        return ItemTags.createOptional(new ResourceLocation("curios", path));
    }
}
