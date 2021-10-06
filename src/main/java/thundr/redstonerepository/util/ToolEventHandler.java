package thundr.redstonerepository.util;

import cofh.core.util.helpers.BaublesHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.item.tool.ItemPickaxeGelid;
import thundr.redstonerepository.item.util.ItemRingEffect;
import thundr.redstonerepository.item.util.ItemRingMining;

import java.util.ArrayList;

public class ToolEventHandler {

    public static int pickaxeDistanceFactor;
    public static int pickaxeDimensionFactor;

    public static void preInit() {
        pickaxeDistanceFactor = RedstoneRepository.CONFIG_COMMON.get("Equipment.Tools", "PickaxeDistanceDrainFactor", 5, "Set the factor that scales the power drained from the Gelid Enderium Pickaxe when teleporting items over a distance. (distance*factor*itemDrops=power)");
        pickaxeDimensionFactor = RedstoneRepository.CONFIG_COMMON.get("Equipment.Tools", "PickaxeDimensionDrainFactor", 7500, "Set the factor that scales the power drained from the Gelid Enderium Pickaxe when teleporting items between dimensions. This is a flat value per item.");
    }

    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        if (!world.isRemote && event.getHarvester() != null && !event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).isEmpty() && event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPickaxeGelid) {
            ItemStack stack = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
            ItemPickaxeGelid pickaxe = (ItemPickaxeGelid) event.getHarvester().getHeldItem(EnumHand.MAIN_HAND).getItem();
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
                    WorldServer boundWorld = DimensionManager.getWorld(dimID);
                    if (event.getWorld().getBlockState(event.getPos()) != boundWorld.getBlockState(new BlockPos(coordX, coordY, coordZ))) {
                        EnumFacing dir;
                        TileEntity bound = boundWorld.getTileEntity(new BlockPos(coordX, coordY, coordZ));
                        if (!bound.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir = EnumFacing.getFront(side))) {
                            return;
                        }
                        IItemHandler inventory = bound.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
                        for (int drop = 0; drop < event.getDrops().size(); ++drop) {
                            ItemStack returned = ItemHandlerHelper.insertItemStacked(inventory, event.getDrops().get(drop), false);
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
            return (int) (Math.sqrt(Math.pow(from.getX() - to.getX(), 2.0) + Math.pow(from.getY() - to.getY(), 2.0) + Math.pow(from.getX() - to.getX(), 2.0)) * (double) pickaxeDistanceFactor);
        }
        return pickaxeDimensionFactor;
    }

    public boolean isEmpowered(ItemStack stack) {
        ItemPickaxeGelid pick = (ItemPickaxeGelid) stack.getItem();
        return pick.getMode(stack) == 1 && pick.getEnergyStored(stack) >= pick.getEnergyPerUseCharged();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote) {
            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                for (ItemStack itemStack : BaublesHelper.getBaubles(player)) {
                    //make sure we have a player with a ring on
                    if (itemStack.getItem() instanceof ItemRingEffect) {
                        ItemRingEffect ring = (ItemRingEffect) itemStack.getItem();
                        if (ring.isActive(itemStack)) {
                            ArrayList<PotionEffect> potions = ring.globalMap.get(player.getUniqueID());
                            if (potions != null) {
                                int diff = player.getActivePotionEffects().size() - potions.size();
                                if (diff > 0) {
                                    ring.useEnergy(itemStack, (int)Math.pow(2, diff + 6.0) , false);
                                    player.clearActivePotions();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockMined(PlayerEvent.BreakSpeed event){
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            for (ItemStack itemStack : BaublesHelper.getBaubles(player)) {
                if (itemStack.getItem() instanceof ItemRingMining) {
                    ItemRingMining ring = (ItemRingMining) itemStack.getItem();

                    if (ring.isActive(itemStack) && !player.onGround) {
                        event.setNewSpeed(event.getOriginalSpeed() * 5.0f);
                        ring.useEnergy(itemStack, 1, false);
                    }
                }
            }
        }
    }
}

