package thundr.redstonerepository.init;

import cofh.core.gui.CreativeTabCore;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;

import javax.annotation.Nonnull;

public class RedstoneRepositoryProps {

    private RedstoneRepositoryProps() {
    }

    public static void preInit() {
        configCommon();
        configClient();
    }

    private static void configCommon() {
    }

    private static void configClient() {
        RedstoneRepository.tabCommon = new CreativeTabCore("redstonerepository") {

            @Nonnull
            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {
                return new ItemStack(RedstoneRepositoryEquipment.ToolSet.GELID.itemSword);
            }
        };
    }

}

