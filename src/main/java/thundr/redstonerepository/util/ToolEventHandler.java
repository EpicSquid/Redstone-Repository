package thundr.redstonerepository.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.items.tools.gelidenderium.ItemPickaxeGelidEnderium;

public class ToolEventHandler {
    public static int pickaxeDistanceFactor;
    public static int pickaxeDimensionFactor;

    public static void preInit() {
        pickaxeDistanceFactor = RedstoneRepository.CONFIG.get("Equipment.Tools", "PickaxeDistanceDrainFactor", 5, "Set the factor that scales the power drained from the Gelid Enderium Pickaxe when teleporting items over a distance. (distance*factor*itemDrops=power)");
        pickaxeDimensionFactor = RedstoneRepository.CONFIG.get("Equipment.Tools", "PickaxeDimensionDrainFactor", 7500, "Set the factor that scales the power drained from the Gelid Enderium Pickaxe when teleporting items between dimensions. This is a flat value per item.");
    }

    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        if (!world.isRemote && event.getHarvester() != null && !event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).isEmpty() && event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPickaxeGelidEnderium) {
            ItemStack stack = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
            ItemPickaxeGelidEnderium pickaxe = (ItemPickaxeGelidEnderium)event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).getItem();
            if (this.isEmpowered(stack)) {
                if (stack.getTagCompound() == null) {
                    stack.setTagCompound(new NBTTagCompound());
                }
                NBTTagCompound tag = stack.getTagCompound();
                int coordX = tag.getInteger("CoordX");
                int coordY = tag.getInteger("CoordY");
                int coordZ = tag.getInteger("CoordZ");
                int dimID = tag.getInteger("DimID");
                int side = tag.getInteger("Side");
                boolean isBound = tag.getBoolean("Bound");
                if (isBound) {
                    WorldServer boundWorld = DimensionManager.getWorld((int)dimID);
                    if (event.getWorld().getBlockState(event.getPos()) != boundWorld.getBlockState(new BlockPos(coordX, coordY, coordZ))) {
                        EnumFacing dir;
                        TileEntity bound = boundWorld.getTileEntity(new BlockPos(coordX, coordY, coordZ));
                        if (!bound.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir = EnumFacing.getFront((int)side))) {
                            return;
                        }
                        IItemHandler inventory = (IItemHandler)bound.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
                        for (int drop = 0; drop < event.getDrops().size(); ++drop) {
                            ItemStack returned = ItemHandlerHelper.insertItemStacked((IItemHandler)inventory, (ItemStack)((ItemStack)event.getDrops().get(drop)), (boolean)false);
                            int temp = this.drainEnergyByDistance(event.getPos(), new BlockPos(coordX, coordY, coordZ), dimID != event.getHarvester().dimension);
                            pickaxe.extractEnergy(stack, temp, false);
                            if (!returned.isEmpty()) {
                                return;
                            }
                            event.setDropChance(0.0f);
                        }
                    }
                }
            }
        }
    }

    private int drainEnergyByDistance(BlockPos from, BlockPos to, boolean interdim) {
        if (!interdim) {
            return (int)(Math.sqrt(Math.pow(from.getX() - to.getX(), 2.0) + Math.pow(from.getY() - to.getY(), 2.0) + Math.pow(from.getX() - to.getX(), 2.0)) * (double)pickaxeDistanceFactor);
        }
        return pickaxeDimensionFactor;
    }

    public boolean isEmpowered(ItemStack stack) {
        ItemPickaxeGelidEnderium pick = (ItemPickaxeGelidEnderium)stack.getItem();
        return pick.getMode(stack) == 1 && pick.getEnergyStored(stack) >= pick.getEnergyPerUseCharged();
    }
}

