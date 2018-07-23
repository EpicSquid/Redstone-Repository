package thundr.redstonerepository.items.tools.gelidenderium;

import cofh.redstonearsenal.item.tool.ItemBattleWrenchFlux;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thundr.redstonerepository.items.tools.gelidenderium.GelidEnderiumEnergy;

public class ItemBattleWrenchGelidEnderium extends ItemBattleWrenchFlux {
    public int radius = 10;

    public ItemBattleWrenchGelidEnderium(Item.ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.damage = 7;
        this.damageCharged = 2;
        this.maxEnergy = GelidEnderiumEnergy.maxEnergy;
        this.energyPerUse = GelidEnderiumEnergy.energyPerUse;
        this.energyPerUseCharged = GelidEnderiumEnergy.energyPerUseCharged;
        this.maxTransfer = GelidEnderiumEnergy.maxTransfer;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        BlockPos pos = player.getPosition();
        if (!world.isRemote && hand == EnumHand.MAIN_HAND && this.isEmpowered(held)) {
            List<EntityItem> items = new ArrayList(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB((double)(pos.getX() - this.radius), (double)(pos.getY() - this.radius), (double)(pos.getZ() - this.radius), (double)(pos.getX() + this.radius), (double)(pos.getY() + this.radius), (double)(pos.getZ() + this.radius))));
            if (items.size() > 0 && this.getEnergyStored(held) >= this.energyPerUseCharged * items.size()) {
                for (EntityItem i : items) {
                    i.setPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                }
                this.extractEnergy(held, this.energyPerUseCharged * items.size(), player.capabilities.isCreativeMode);
            }
            player.swingArm(EnumHand.MAIN_HAND);
            return new ActionResult(EnumActionResult.SUCCESS, (Object)player.getHeldItem(hand));
        }
        return new ActionResult(EnumActionResult.FAIL, (Object)player.getHeldItem(hand));
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}

