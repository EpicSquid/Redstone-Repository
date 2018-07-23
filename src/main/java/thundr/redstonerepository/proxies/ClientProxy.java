/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  cofh.core.render.IModelRegister
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 */
package thundr.redstonerepository.proxies;

import cofh.core.render.IModelRegister;
import java.util.ArrayList;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thundr.redstonerepository.proxies.CommonProxy;

public class ClientProxy
extends CommonProxy {
    private static ArrayList<IModelRegister> modelList = new ArrayList();

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

