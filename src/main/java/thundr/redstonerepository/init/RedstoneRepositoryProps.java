/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  cofh.core.gui.CreativeTabCore
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package thundr.redstonerepository.init;

import cofh.core.gui.CreativeTabCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.items.tools.gelidenderium.ItemSwordGelidEnderium;

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
        RedstoneRepository.tabCommon = new CreativeTabCore("redstonerepository"){

            @SideOnly(value=Side.CLIENT)
            public ItemStack func_151244_d() {
                return new ItemStack((Item)RedstoneRepositoryEquipment.ToolSet.GELID.itemSword);
            }
        };
    }

}

