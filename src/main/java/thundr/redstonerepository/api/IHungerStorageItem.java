package thundr.redstonerepository.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHungerStorageItem {
    public int receiveHungerPoints(ItemStack var1, int var2, boolean var3);

    public int useHungerPoints(ItemStack var1, int var2, EntityPlayer var3);

    public int getHungerPoints(ItemStack var1);

    public int getMaxHungerPoints(ItemStack var1);
}

