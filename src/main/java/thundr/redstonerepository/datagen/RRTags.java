package thundr.redstonerepository.datagen;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import thundr.redstonerepository.RedstoneRepository;

public class RRTags {

    public static final ITag.INamedTag<Item> RR_TAG = mod("redstone_repository_tag");

    public static final ITag.INamedTag<Item> CURIOS_HEAD = curios("head");
    public static final ITag.INamedTag<Item> CURIOS_BELT = curios("belt");

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
