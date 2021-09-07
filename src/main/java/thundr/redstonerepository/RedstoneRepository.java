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
import net.minecraftforge.fml.common.network.IGuiHandler;
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

@Mod(modid = "redstonerepository", name = "Redstone Repository", version = "1.3.2", dependencies = "required-after:redstonearsenal@[2.3.11,);required-after:cofhcore@[4.3.11,);required-after:thermalfoundation@[2.3.11,);required-after:thermalexpansion@[5.3.11,);after:baubles;", guiFactory = "thundr.redstonerepository.gui.ConfigGuiFactory", acceptedMinecraftVersions = "[1.12.2]")
public class RedstoneRepository {
    public static final String NAME = "Redstone Repository";
    public static final String ID = "redstonerepository";
    public static final String VERSION = "1.3.2";
    public static final String GUIFACTORY = "thundr.redstonerepository.gui.ConfigGuiFactory";
    public static final String REQUIRED = "required-after:redstonearsenal@[2.3.11,);required-after:cofhcore@[4.3.11,);required-after:thermalfoundation@[2.3.11,);required-after:thermalexpansion@[5.3.11,);after:baubles;";
    public static final String CLIENTPROXY = "thundr.redstonerepository.proxies.ClientProxy";
    public static final String COMMONPROXY = "thundr.redstonerepository.proxies.CommonProxy";
    public static final Logger LOG;
    public static final ConfigHandler CONFIG;
    public static final ConfigHandler CONFIG_CLIENT;
    @Mod.Instance(value = "redstonerepository")
    public static RedstoneRepository instance;
    @SidedProxy(clientSide = "thundr.redstonerepository.proxies.ClientProxy", serverSide = "thundr.redstonerepository.proxies.CommonProxy")
    public static CommonProxy proxy;
    public static CreativeTabs tabCommon;

    static {
        LOG = LogManager.getLogger("redstonerepository");
        CONFIG = new ConfigHandler("1.3.2");
        CONFIG_CLIENT = new ConfigHandler("1.3.2");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG.setConfiguration(new Configuration(new File(CoreProps.configDir, "/redstonerepository/common.cfg"), true));
        CONFIG_CLIENT.setConfiguration(new Configuration(new File(CoreProps.configDir, "/redstonerepository/client.cfg"), true));
        RedstoneRepositoryProps.preInit();
        RedstoneRepositoryBlocks.preInit();
        RedstoneRepositoryItems.preInit();
        RedstoneRepositoryEquipment.preInit();
        ArmorEventHandler.preInit();
        ToolEventHandler.preInit();
        PacketHandler.INSTANCE.registerPacket(PacketRR.class);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        proxy.initialize(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CONFIG.cleanUp(false, true);
        CONFIG_CLIENT.cleanUp(false, true);
        LOG.info("Redstone Repository: Loaded.");
    }
}

