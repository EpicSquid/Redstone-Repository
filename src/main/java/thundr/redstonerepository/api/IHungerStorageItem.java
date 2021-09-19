package thundr.redstonerepository.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHungerStorageItem {

    int receiveHungerPoints(ItemStack stack, int amount, boolean simulate);

    int useHungerPoints(ItemStack stack, int amount, EntityPlayer player);

    int getHungerPoints(ItemStack stack);

    int getMaxHungerPoints(ItemStack stack);
}

