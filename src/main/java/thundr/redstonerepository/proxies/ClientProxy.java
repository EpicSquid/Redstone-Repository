package thundr.redstonerepository.proxies;

import cofh.core.render.IModelRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy {

    private static final ArrayList<IModelRegister> modelList = new ArrayList();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        for (IModelRegister register : modelList) {
            register.registerModels();
        }
    }

    @Override
    public void initialize(FMLInitializationEvent event) {
        super.initialize(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public boolean addIModelRegister(IModelRegister modelRegister) {
        return modelList.add(modelRegister);
    }
}

