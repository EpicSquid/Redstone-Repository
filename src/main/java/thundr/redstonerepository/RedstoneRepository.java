package thundr.redstonerepository;

import cofh.core.init.CoreProps;
import cofh.core.network.PacketHandler;
import cofh.core.util.ConfigHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
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
import thundr.redstonerepository.proxy.CommonProxy;
import thundr.redstonerepository.util.ArmorEventHandler;
import thundr.redstonerepository.util.ToolEventHandler;

import java.io.File;

@Mod(modid = RedstoneRepository.MODID, name = RedstoneRepository.MODNAME, dependencies = RedstoneRepository.DEPENDENCIES, guiFactory = RedstoneRepository.GUI_FACTORY)
//, acceptedMinecraftVersions = "[1.12.2]")
public class RedstoneRepository {

    public static final String MODID = "redstonerepository";
    public static final String MODNAME = "Redstone Repository";

    public static final String VERSION = "${version}";

    public static final String DEPENDENCIES = "required-after:redstonearsenal@[2.3.11,);required-after:cofhcore@[4.3.11,);required-after:thermalfoundation@[2.3.11,);required-after:thermalexpansion@[5.3.11,);after:baubles;";
    public static final String GUI_FACTORY = "thundr.redstonerepository.gui.ConfigGuiFactory";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ConfigHandler CONFIG_COMMON = new ConfigHandler(VERSION);
    public static final ConfigHandler CONFIG_CLIENT = new ConfigHandler(VERSION);

    @Instance(MODID)
    public static RedstoneRepository INSTANCE;

    @SidedProxy(clientSide = "thundr.redstonerepository.proxy.ClientProxy", serverSide = "thundr.redstonerepository.proxy.CommonProxy")
    public static CommonProxy PROXY;

    public static CreativeTabs tabCommon;

    public RedstoneRepository() {
        super();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Redstone Repository: Pre-Initialization");

        CONFIG_COMMON.setConfiguration(new Configuration(new File(CoreProps.configDir, "/" + MODID + "/common.cfg"), true));
        CONFIG_CLIENT.setConfiguration(new Configuration(new File(CoreProps.configDir, "/" + MODID + "/client.cfg"), true));

        RedstoneRepositoryProps.preInit();

        RedstoneRepositoryBlocks.preInit();
        RedstoneRepositoryItems.preInit();
        RedstoneRepositoryEquipment.preInit();

        //CoreEnchantments.register();
        ArmorEventHandler.preInit();
        ToolEventHandler.preInit();

        PacketHandler.INSTANCE.registerPacket(PacketRR.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());

        PROXY.preInit(event);
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        LOGGER.info("Redstone Repository: Initialization");
        PROXY.initialize(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.info("Redstone Repository: Post-Initialization");
        PROXY.postInit(event);
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CONFIG_COMMON.cleanUp(false, true);
        CONFIG_CLIENT.cleanUp(false, true);
        LOGGER.info("Redstone Repository: Load Complete");
    }
}

