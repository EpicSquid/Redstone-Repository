package tomheaton.redstonerepository.init;

import cofh.core.gui.CreativeTabCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tomheaton.redstonerepository.RedstoneRepository;

public class RedstoneRepositoryProps {

    public static final String PATH_GUI = "redstonerepository:textures/gui/";
    public static final ResourceLocation FEEDER_GUI_STORAGE = new ResourceLocation("redstonerepository:textures/gui/storage_1.png");

    private RedstoneRepositoryProps() {
    }

    public static void preInit() {
        RedstoneRepositoryProps.configCommon();
        RedstoneRepositoryProps.configClient();
    }

    private static void configCommon() {
    }

    private static void configClient() {
        /*RedstoneRepository.tabCommon = new CreativeTabCore("redstonerepository") {

            @SideOnly(value = Side.CLIENT)
            public ItemStack getIcon() {
                return new ItemStack(RedstoneRepositoryEquipment.ToolSet.GELID.itemSword);
            }
        };*/
    }

}

