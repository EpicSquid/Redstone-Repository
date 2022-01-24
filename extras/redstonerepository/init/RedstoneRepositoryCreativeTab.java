package tomheaton.redstonerepository.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tomheaton.redstonerepository.RedstoneRepository;

import javax.annotation.Nonnull;

public class RedstoneRepositoryCreativeTab extends CreativeTabs {

    public RedstoneRepositoryCreativeTab() {
        super(RedstoneRepository.MODID + ".creativeTab");
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(RedstoneRepositoryEquipment.ToolSet.GELID.itemSword);
    }
}
