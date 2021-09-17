package thundr.redstonerepository.item.tool.gelidenderium;

import cofh.redstonearsenal.item.tool.ItemBattleWrenchFlux;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;

@Optional.InterfaceList ({ @Optional.Interface (iface = "buildcraft.api.tools.IToolWrench", modid = "buildcraftcore"), @Optional.Interface (iface = "crazypants.enderio.api.tool.ITool", modid = "enderio") })
public class ItemBattleWrenchGelid extends ItemBattleWrenchFlux {

    public int radius = 10;

    public ItemBattleWrenchGelid(ToolMaterial toolMaterial) {
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
            List<EntityItem> items = new ArrayList(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - this.radius, pos.getY() - this.radius, pos.getZ() - this.radius, pos.getX() + this.radius, pos.getY() + this.radius, pos.getZ() + this.radius)));
            if (items.size() > 0 && this.getEnergyStored(held) >= this.energyPerUseCharged * items.size()) {
                for (EntityItem i : items) {
                    i.setPosition(pos.getX(), pos.getY(), pos.getZ());
                }
                this.extractEnergy(held, this.energyPerUseCharged * items.size(), player.capabilities.isCreativeMode);
            }
            player.swingArm(EnumHand.MAIN_HAND);
            return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return new ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand));
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }

    //@Optional.Method(modid = "buildcraftcore")
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    //@Optional.Method(modid = "buildcraftcore")
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
    }

}

