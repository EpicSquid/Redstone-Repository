package thundr.redstonerepository.util;

import net.minecraft.entity.player.EntityPlayer;
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

    // Returns the value of an ItemStack of food in Hunger Points each point of food and saturation is a hunger point
    public static int findHungerValues(ItemStack food) {
        if (!(food.getItem() instanceof ItemFood)) {
            return 0;
        }
        return food.getCount() * HungerHelper.findHungerValueSingle(food);
    }

    // Finds a single food item's value.
    public static int findHungerValueSingle(ItemStack food) {
        if (!(food.getItem() instanceof ItemFood)) {
            return 0;
        }
        ItemFood itemFood = (ItemFood) food.getItem();
        return Math.max(1, (int) ((float) itemFood.getHealAmount(food) * itemFood.getSaturationModifier(food) * 2.0f + (float) itemFood.getHealAmount(food)));
    }

    public static void addHunger(EntityPlayer player, int amount) {
        // Only add hunger points, not saturation. This automatically caps at 20 due to addStats logic.
        player.getFoodStats().addStats(amount, 0.0f);
    }

    public static void addSaturation(EntityPlayer player, int amount) {
        FoodStats foodStats = player.getFoodStats();
        // Cap saturation at 20 points.
        foodStats.addStats(amount, 0.5f);
    }
}

