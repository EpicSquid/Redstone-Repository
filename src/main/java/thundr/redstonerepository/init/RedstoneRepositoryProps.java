package thundr.redstonerepository.init;

import cofh.core.gui.CreativeTabCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;

import javax.annotation.Nonnull;

public class RedstoneRepositoryProps {

    //public static final String PATH_GUI = "redstonerepository:textures/gui/";
    //public static final ResourceLocation FEEDER_GUI_STORAGE = new ResourceLocation("redstonerepository:textures/gui/storage_1.png");
    public static final ResourceLocation FEEDER_GUI_STORAGE = new ResourceLocation(RedstoneRepository.MODID,"textures/gui/storage_1.png");

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
            @SideOnly(value = Side.CLIENT)
            public ItemStack getTabIconItem() {
                return new ItemStack(RedstoneRepositoryEquipment.ToolSet.GELID.itemSword);
            }
        };
    }

}

