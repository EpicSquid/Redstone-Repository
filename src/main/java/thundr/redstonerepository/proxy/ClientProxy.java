package thundr.redstonerepository.proxy;

import cofh.core.render.IModelRegister;
import cofh.core.render.entity.RenderArrowCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.entity.projectile.EntityArrowGelid;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {

    private static final ArrayList<IModelRegister> modelList = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        for (IModelRegister register : modelList) {
            register.registerModels();
        }
        registerRenderInformation();
    }

    @Override
    public void initialize(FMLInitializationEvent event) {
        super.initialize(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void registerRenderInformation() {
        RenderingRegistry.registerEntityRenderingHandler(EntityArrowGelid.class, manager -> new RenderArrowCore(manager, new ResourceLocation(RedstoneRepository.MODID, "textures/entity/arrow_gelid.png")));
    }

    public boolean addIModelRegister(IModelRegister modelRegister) {
        return modelList.add(modelRegister);
    }
}

