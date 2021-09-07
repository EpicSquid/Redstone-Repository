package thundr.redstonerepository.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHungerStorageItem {
    int receiveHungerPoints(ItemStack var1, int var2, boolean var3);

    int useHungerPoints(ItemStack var1, int var2, EntityPlayer var3);

    int getHungerPoints(ItemStack var1);

    int getMaxHungerPoints(ItemStack var1);
}

