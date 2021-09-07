package thundr.redstonerepository.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import thundr.redstonerepository.RedstoneRepository;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, ConfigGui.getConfigElements(parentScreen), RedstoneRepository.MODID, false, false, GuiConfig.getAbridgedConfigPath(RedstoneRepository.CONFIG_COMMON.getConfiguration().toString()));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
        for (String section : RedstoneRepository.CONFIG_COMMON.getCategoryNames()) {
            ConfigCategory category = RedstoneRepository.CONFIG_COMMON.getCategory(section);
            if (category.isChild()) continue;
            list.add(new ConfigElement(category));
        }
        return list;
    }
}

