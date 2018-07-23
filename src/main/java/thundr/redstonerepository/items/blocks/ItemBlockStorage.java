/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  cofh.core.block.ItemBlockCore
 *  cofh.core.util.helpers.ItemHelper
 *  net.minecraft.block.Block
 *  net.minecraft.item.EnumRarity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 */
package thundr.redstonerepository.items.blocks;

import cofh.core.block.ItemBlockCore;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thundr.redstonerepository.blocks.BlockStorage;

public class ItemBlockStorage
extends ItemBlockCore {
    public ItemBlockStorage(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return "tile.redstonerepository.storage." + BlockStorage.Type.byMetadata(ItemHelper.getItemDamage((ItemStack)stack)).getNameRaw() + ".name";
    }

    public EnumRarity getRarity(ItemStack stack) {
        return BlockStorage.Type.byMetadata(ItemHelper.getItemDamage((ItemStack)stack)).getRarity();
    }
}

