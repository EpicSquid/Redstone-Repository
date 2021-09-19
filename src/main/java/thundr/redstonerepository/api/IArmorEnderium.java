package thundr.redstonerepository.api;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

public interface IArmorEnderium extends IEnergyContainerItem {

    boolean isEnderiumArmor(ItemStack stack);
}

