package thundr.redstonerepository.item.tool;

import cofh.redstonearsenal.item.tool.ItemAxeFlux;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thundr.redstonerepository.item.GelidEnderiumEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemAxeGelid extends ItemAxeFlux {

    public static final int LIGHTNING_ENERGY = 6400;
    public static final int EMPOWERED_LIGHTNING_ENERGY = 48000;
    public static int blocksPerTick;

    public ItemAxeGelid(Item.ToolMaterial toolMaterial, int axeBlocksCutPerTick) {
        super(toolMaterial);
        this.maxEnergy = GelidEnderiumEnergy.maxEnergy;
        this.energyPerUse = GelidEnderiumEnergy.energyPerUse;
        this.energyPerUseCharged = GelidEnderiumEnergy.energyPerUseCharged;
        this.maxTransfer = GelidEnderiumEnergy.maxTransfer;
        this.damage = 10;
        blocksPerTick = axeBlocksCutPerTick;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        Random rand = new Random();
        ItemStack held = player.getHeldItem(hand);
        if (!world.isRemote && hand == EnumHand.MAIN_HAND && this.isEmpowered(held) && (world.isRaining() || world.isThundering())) {
            WorldInfo worldinfo = world.getWorldInfo();
            int i = 300 + rand.nextInt(600) * 20;
            worldinfo.setRaining(false);
            worldinfo.setThundering(false);
            worldinfo.setRainTime(i);
            world.spawnEntity(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, true));
            if (!player.capabilities.isCreativeMode) {
                this.useEnergy(held, false);
            }
        }
        player.swingArm(EnumHand.MAIN_HAND);
        return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (this.getEnergyStored(held) > 6400) {
            if (!this.isEmpowered(held)) {
                world.spawnEntity(new EntityLightningBolt(world, x, y, z, false));
                if (!player.capabilities.isCreativeMode) {
                    this.extractEnergy(held, 6400, false);
                }
            } else if (this.isEmpowered(held) && this.getEnergyStored(held) >= 48000) {
                for (int i = 0; i <= 10; ++i) {
                    world.spawnEntity(new EntityLightningBolt(world, x, y, z, false));
                }
                if (!player.capabilities.isCreativeMode) {
                    this.extractEnergy(held, 48000, false);
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Block block = state.getBlock();
        float refStrength = state.getPlayerRelativeBlockHardness(player, world, pos);
        if (refStrength != 0.0f && this.isEmpowered(stack) && block.isWood(world, pos) && this.canHarvestBlock(state, stack)) {
            if (!world.isRemote) {
                MinecraftForge.EVENT_BUS.register(new CutTreeTask(stack, pos, player));
            }
            return true;
        }
        return false;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("\u00a7aRight click a block to call down the power of the sky.");
        tooltip.add("\u00a7aRight click the air to clear the skies when empowered.");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 1333581;
    }

    protected boolean harvestBlock(World world, BlockPos pos, EntityPlayer player) {
        if (world.isAirBlock(pos)) {
            return false;
        }
        EntityPlayerMP playerMP = null;
        if (player instanceof EntityPlayerMP) {
            playerMP = (EntityPlayerMP) player;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!this.toolClasses.contains(state.getBlock().getHarvestTool(state)) && !this.canHarvestBlock(state, player.getHeldItemMainhand())) {
            return false;
        }
        if (!ForgeHooks.canHarvestBlock(block, player, world, pos)) {
            return false;
        }
        if (!world.isRemote) {
            int xpToDrop = 0;
            if (playerMP != null && (xpToDrop = ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos)) == -1) {
                return false;
            }
            if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
                if (!player.capabilities.isCreativeMode) {
                    block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
                    if (xpToDrop > 0) {
                        block.dropXpOnBlockBreak(world, pos, xpToDrop);
                    }
                }
            }
            playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
        } else {
            if (block.removedByPlayer(state, world, pos, player, !player.capabilities.isCreativeMode)) {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        return true;
    }

    public static class CutTreeTask {
        public World world;
        public ItemStack tool;
        public ItemAxeGelid axe;
        public BlockPos pos;
        public EntityPlayer player = null;
        public int maxIterations = 10000;
        public int iterationCount = 0;
        public Queue<BlockPos> candidates = new LinkedList<BlockPos>();
        public HashSet<BlockPos> visited = new HashSet();

        public CutTreeTask(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
            if (!(stack.getItem() instanceof ItemAxeGelid)) {
                this.unregister();
                return;
            }
            this.tool = stack;
            this.axe = (ItemAxeGelid) stack.getItem();
            this.pos = pos;
            this.player = player;
            this.world = player.getEntityWorld();
            this.candidates.add(pos);
        }

        @SubscribeEvent
        public void cutTree(TickEvent.WorldTickEvent event) {
            if (event.side.isClient()) {
                this.unregister();
                return;
            }
            if (event.world.provider.getDimension() != this.world.provider.getDimension()) {
                return;
            }
            if (this.axe == null) {
                this.unregister();
                return;
            }
            if (this.axe.getEnergyStored(this.tool) < this.axe.getEnergyPerUse(this.tool)) {
                this.unregister();
                return;
            }
            int blocksIter = ItemAxeGelid.blocksPerTick;
            while (blocksIter > 0) {
                float refStrength;
                EnumFacing[] cardinals;
                BlockPos newPos;
                IBlockState state;
                if (this.candidates.isEmpty()) {
                    this.unregister();
                    return;
                }
                BlockPos curPos = this.candidates.remove();
                if (!this.visited.add(curPos) || !(state = this.world.getBlockState(curPos)).getBlock().isWood(this.world, curPos) || (refStrength = state.getPlayerRelativeBlockHardness(this.player, this.world, curPos)) == 0.0f)
                    continue;
                for (EnumFacing face : cardinals = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}) {
                    newPos = curPos.offset(face);
                    if (this.visited.contains(newPos)) continue;
                    this.candidates.add(newPos);
                }
                int y = 1;
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        newPos = curPos.add(x, y, z);
                        if (this.visited.contains(newPos)) continue;
                        this.candidates.add(newPos);
                    }
                }
                this.axe.harvestBlock(this.world, curPos, this.player);
                this.world.playSound(null, curPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (!this.player.capabilities.isCreativeMode && this.axe.useEnergy(this.tool, false) == 0) break;
                ++this.iterationCount;
                if (this.iterationCount > this.maxIterations) {
                    this.unregister();
                    return;
                }
                --blocksIter;
            }
        }

        private void unregister() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

}

