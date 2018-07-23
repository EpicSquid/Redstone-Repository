package thundr.redstonerepository.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

public class HungerHelper {
    public static ItemStack setDefaultHungerTag(ItemStack container) {
        if (!container.hasTagCompound()) {
            container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setInteger("Hunger", 0);
        }
        return container;
    }

    public static int findHungerValues(ItemStack food) {
        if (!(food.getItem() instanceof ItemFood)) {
            return 0;
        }
        return food.getCount() * HungerHelper.findHungerValueSingle(food);
    }

    public static int findHungerValueSingle(ItemStack food) {
        if (!(food.getItem() instanceof ItemFood)) {
            return 0;
        }
        ItemFood itemFood = (ItemFood)food.getItem();
        return Math.max(1, (int)((float)itemFood.getHealAmount(food) * itemFood.getSaturationModifier(food) * 2.0f + (float)itemFood.getHealAmount(food)));
    }

    public static void addHunger(EntityPlayer player, int amount) {
        player.getFoodStats().addStats(amount, 0.0f);
    }

    public static void addSaturation(EntityPlayer player, int amount) {
        FoodStats foodStats = player.getFoodStats();
        foodStats.addStats(amount, 0.5f);
    }
}

