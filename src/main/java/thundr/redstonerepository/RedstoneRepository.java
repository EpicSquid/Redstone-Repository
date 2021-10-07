package thundr.redstonerepository;

import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thundr.redstonerepository.config.RedstoneRepositoryConfig;
import thundr.redstonerepository.handlers.RegistryHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.stream.Collectors;

@Mod(RedstoneRepository.MODID)
public class RedstoneRepository {

    public static RedstoneRepository INSTANCE;

    public static final String MODID = "redstonerepository";
    public static final String MODNAME = "Redstone Repository";
    public static final String VERSION = "${version}";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final RRItemGroup tabRedstoneRepository = new RRItemGroup();

    public static final ResourceLocation RR_SLOT = new ResourceLocation(MODID, "gui/empty_rr_slot");

    public RedstoneRepository() {
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTextureStitch));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        if (ModList.get().isLoaded("curios")) {
            //MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapabilities);
        }

        MinecraftForge.EVENT_BUS.register(this);

        RedstoneRepositoryConfig.register();
        RegistryHandler.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common Setup Method registered.");
        //NetworkHandler.registerMessages();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Client Setup Method registered.");
        //KeybindHandler.setup();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            InterModComms.sendTo(MODID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
            InterModComms.sendTo(MODID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
            InterModComms.sendTo(MODID, CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        }
    }

    private void processIMC(final InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    private void onTextureStitch(TextureStitchEvent.Pre event) {
        if (ModList.get().isLoaded("curios")) {
            if (event.getMap().location().equals(PlayerContainer.BLOCK_ATLAS)) {
                event.addSprite(RR_SLOT);
            }
        }
    }

    private void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (!ModList.get().isLoaded("curios")) {
            return;
        }
        ItemStack stack = event.getObject();
        /*if (stack.getItem() instanceof TestItem) {
            event.addCapability(CuriosCapability.ID_ITEM, CuriosIntegration.initTestItemCapabilities(stack));
        }*/
    }
}