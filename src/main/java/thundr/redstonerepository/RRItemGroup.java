package thundr.redstonerepository;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class RRItemGroup extends ItemGroup {

    public RRItemGroup() {
        super(RedstoneRepository.MODID + ".main");
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.REDSTONE);
    }
}
