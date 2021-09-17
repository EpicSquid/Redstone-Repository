package thundr.redstonerepository.item.tool.gelidenderium;

import cofh.redstonearsenal.item.tool.ItemShovelFlux;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.List;

public class ItemShovelGelid extends ItemShovelFlux {

    public ItemShovelGelid(Item.ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.maxEnergy = GelidEnderiumEnergy.maxEnergy;
        this.energyPerUse = GelidEnderiumEnergy.energyPerUse;
        this.energyPerUseCharged = GelidEnderiumEnergy.energyPerUseCharged;
        this.maxTransfer = GelidEnderiumEnergy.maxTransfer;
    }

    /*
     * Enabled aggressive block sorting
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.isSneaking() && stack.getItem() instanceof ItemShovelGelid) {
            ItemShovelGelid shovel = (ItemShovelGelid) stack.getItem();
            if (!player.canPlayerEdit(pos, facing, stack)) {
                return EnumActionResult.FAIL;
            }
            if (!this.isEmpowered(stack) && this.getEnergyStored(stack) > this.energyPerUseCharged) {
                this.growCrop(worldIn, pos, player, stack, facing, hand, this.energyPerUseCharged);
                return EnumActionResult.SUCCESS;
            }
            if (!this.isEmpowered(stack) || this.getEnergyStored(stack) <= this.energyPerUseCharged * 9) {
                if (!player.capabilities.isCreativeMode) return EnumActionResult.FAIL;
            }
        } else {
            super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            return EnumActionResult.SUCCESS;
        }
        int x = -1;
        while (x < 2) {
            for (int z = -1; z < 2; ++z) {
                BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                this.growCrop(worldIn, newPos, player, stack, facing, hand, this.energyPerUseCharged * 9);
            }
            ++x;
        }
        return EnumActionResult.SUCCESS;
    }

    private void growCrop(World world, BlockPos pos, EntityPlayer player, ItemStack stack, EnumFacing facing, EnumHand hand, int energy) {
        Block block = player.world.getBlockState(pos).getBlock();
        if (!world.isRemote && (block instanceof IGrowable || block instanceof IPlantable || block == Blocks.MYCELIUM)) {
            if (world.rand.nextBoolean()) {
                world.scheduleBlockUpdate(pos, block, 0, 100);
            }
            world.scheduleBlockUpdate(pos, block, 0, 100);
            if (!player.capabilities.isCreativeMode) {
                this.useEnergy(stack, false);
            }
            if (world.rand.nextFloat() >= 0.9f) {
                world.playEvent(2005, pos, 0);
            }
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("\u00a7aRight click to rapidly grow crops. Works better when empowered.");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }
}

