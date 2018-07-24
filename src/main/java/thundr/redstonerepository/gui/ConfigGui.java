package thundr.redstonerepository.gui;

import cofh.core.util.ConfigHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import thundr.redstonerepository.RedstoneRepository;

public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, ConfigGui.getConfigElements(parentScreen), "redstonerepository", false, false, GuiConfig.getAbridgedConfigPath((String)RedstoneRepository.CONFIG.getConfiguration().toString()));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
        for (String section : RedstoneRepository.CONFIG.getCategoryNames()) {
            ConfigCategory category = RedstoneRepository.CONFIG.getCategory(section);
            if (category.isChild()) continue;
            list.add((IConfigElement)new ConfigElement(category));
        }
        return list;
    }
}

