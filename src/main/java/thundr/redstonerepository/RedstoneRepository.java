package thundr.redstonerepository;

import cofh.core.init.CoreProps;
import cofh.core.network.PacketHandler;
import cofh.core.util.ConfigHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thundr.redstonerepository.gui.GuiHandler;
import thundr.redstonerepository.init.RedstoneRepositoryBlocks;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.init.RedstoneRepositoryItems;
import thundr.redstonerepository.init.RedstoneRepositoryProps;
import thundr.redstonerepository.network.PacketRR;
import thundr.redstonerepository.proxies.CommonProxy;
import thundr.redstonerepository.util.ArmorEventHandler;
import thundr.redstonerepository.util.ToolEventHandler;

import java.io.File;

@Mod(modid = RedstoneRepository.MODID, dependencies = RedstoneRepository.DEPENDENCIES, guiFactory = "thundr.redstonerepository.gui.ConfigGuiFactory", acceptedMinecraftVersions = "[1.12.2]")
public class RedstoneRepository {

    public static final String MODID = "redstonerepository";
    public static final String MODNAME = "Redstone Repository";
    public static final String VERSION = "${version}";

    public static final String DEPENDENCIES = "required-after:redstonearsenal@[2.3.11,);required-after:cofhcore@[4.3.11,);required-after:thermalfoundation@[2.3.11,);required-after:thermalexpansion@[5.3.11,);after:baubles;";

    public static final Logger LOGGER;
    public static final ConfigHandler CONFIG_COMMON;
    public static final ConfigHandler CONFIG_CLIENT;
    @Mod.Instance(MODID)
    public static RedstoneRepository INSTANCE;
    @SidedProxy(clientSide = "thundr.redstonerepository.proxies.ClientProxy", serverSide = "thundr.redstonerepository.proxies.CommonProxy")
    public static CommonProxy PROXY;
    public static CreativeTabs tabCommon;

    static {
        LOGGER = LogManager.getLogger(MODID);
        CONFIG_COMMON = new ConfigHandler(VERSION);
        CONFIG_CLIENT = new ConfigHandler(VERSION);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG_COMMON.setConfiguration(new Configuration(new File(CoreProps.configDir, "/redstonerepository/common.cfg"), true));
        CONFIG_CLIENT.setConfiguration(new Configuration(new File(CoreProps.configDir, "/redstonerepository/client.cfg"), true));
        RedstoneRepositoryProps.preInit();
        RedstoneRepositoryBlocks.preInit();
        RedstoneRepositoryItems.preInit();
        RedstoneRepositoryEquipment.preInit();
        ArmorEventHandler.preInit();
        ToolEventHandler.preInit();
        PacketHandler.INSTANCE.registerPacket(PacketRR.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        PROXY.initialize(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CONFIG_COMMON.cleanUp(false, true);
        CONFIG_CLIENT.cleanUp(false, true);
        LOGGER.info("Redstone Repository: Loaded.");
    }
}

