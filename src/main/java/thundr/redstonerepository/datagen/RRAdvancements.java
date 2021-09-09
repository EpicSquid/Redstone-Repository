package thundr.redstonerepository.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.handlers.RegistryHandler;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RRAdvancements implements Consumer<Consumer<Advancement>> {

    // showToast, announceChat, hidden

    public RRAdvancements() {
    }

    @Override
    public void accept(Consumer<Advancement> consumer) {
        // Redstone Repository:
        Advancement root = rootAdvancement(consumer, RegistryHandler.TEST.get(), "root", null, "stone", FrameType.TASK, "crafting_table", InventoryChangeTrigger.Instance.hasItems(Blocks.CRAFTING_TABLE));
        Advancement test = itemAdvancement(consumer, root, RegistryHandler.TEST.get(), "redstonerepository", FrameType.CHALLENGE);
        Advancement test_two = itemAdvancement(consumer, root, RegistryHandler.TEST_TWO.get(), "redstonerepository", FrameType.CHALLENGE);
}

    private Advancement itemAdvancement(Consumer<Advancement> consumer, Advancement parent, IItemProvider item, String path, FrameType frame) {
        String name = item.asItem().getRegistryName().getPath();
        return Advancement.Builder.advancement().parent(parent)
                .display(item,
                        new TranslationTextComponent("advancement.redstonerepository." + name + ".title"),
                        new TranslationTextComponent("advancement.redstonerepository." + name + ".description"),
                        null, frame, true, true, false
                )
                .addCriterion("has_item", InventoryChangeTrigger.Instance.hasItems(item))
                .save(consumer, new ResourceLocation(RedstoneRepository.MODID, (path == null ? "" : path + "/") + name).toString());
    }

    private Advancement advancement(Consumer<Advancement> consumer, Advancement parent, IItemProvider item, String path, FrameType frame, String criterionId, @Nullable ICriterionInstance criterion) {
        String name = item.asItem().getRegistryName().getPath();
        return Advancement.Builder.advancement().parent(parent)
                .display(item,
                        new TranslationTextComponent("advancement.redstonerepository." + name + ".title"),
                        new TranslationTextComponent("advancement.redstonerepository." + name + ".description"),
                        null, frame, true, true, false
                )
                .addCriterion(criterionId, criterion == null ? InventoryChangeTrigger.Instance.hasItems(item) : criterion)
                .save(consumer, new ResourceLocation(RedstoneRepository.MODID, (path == null ? "" : path + "/") + name).toString());
    }

    private Advancement rootAdvancement(Consumer<Advancement> consumer, IItemProvider item, String key, @Nullable String path, String background, FrameType frame, String criterionId, ICriterionInstance criterion) {
        return Advancement.Builder.advancement()
                .display(item,
                        new TranslationTextComponent("advancement.redstonerepository." + key + ".title"),
                        new TranslationTextComponent("advancement.redstonerepository." + key + ".description"),
                        new ResourceLocation("textures/gui/advancements/backgrounds/" + background + ".png"),
                        frame, true, false, false)
                .addCriterion("any", PositionTrigger.Instance.located(LocationPredicate.ANY))
                //.addCriterion(criterionId, criterion)
                .save(consumer, new ResourceLocation(RedstoneRepository.MODID, (path == null ? "" : path + "/") + key).toString());
    }
}
