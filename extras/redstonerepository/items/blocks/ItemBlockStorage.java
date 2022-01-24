package tomheaton.redstonerepository.item.blocks;

import cofh.core.block.ItemBlockCore;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import tomheaton.redstonerepository.blocks.BlockStorage;

public class ItemBlockStorage extends ItemBlockCore {

    public ItemBlockStorage(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return "tile.redstonerepository.storage." + BlockStorage.Type.values()[ItemHelper.getItemDamage(stack)].getNameRaw() + ".name";
    }

    public EnumRarity getRarity(ItemStack stack) {
        return BlockStorage.Type.values()[ItemHelper.getItemDamage(stack)].getRarity();
    }
}

