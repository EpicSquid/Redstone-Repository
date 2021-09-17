package thundr.redstonerepository.item.tool.gelidenderium;

import cofh.redstonearsenal.item.tool.ItemSickleFlux;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSickleGelid extends ItemSickleFlux {

    public ItemSickleGelid(Item.ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.maxEnergy = GelidEnderiumEnergy.maxEnergy;
        this.energyPerUse = GelidEnderiumEnergy.energyPerUse;
        this.energyPerUseCharged = GelidEnderiumEnergy.energyPerUseCharged;
        this.maxTransfer = GelidEnderiumEnergy.maxTransfer;
        this.radius = 5;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}

