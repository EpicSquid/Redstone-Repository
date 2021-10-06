package thundr.redstonerepository.item.util;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cofh.core.key.KeyBindingItemMultiMode;
import cofh.core.util.CoreUtils;
import cofh.core.util.helpers.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.init.RedstoneRepositoryEquipment;
import thundr.redstonerepository.item.ItemCoreRF;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRingEffect extends ItemCoreRF implements IBauble {

    public final static int POTION_DURATION_TICKS = 290;

    public static final String POWER_TICK = "pwrTick";
    public static final String UNTIL_SAFE_TO_REMOVE = "cd";
    public static final String EFFECTS = "efx";
    public static final String AMPLIFIER = "amp";
    public static final String ON_COOLDOWN = "cd2";

    public ConcurrentHashMap<UUID, ArrayList<PotionEffect>> globalMap;

    public static int removalTimer = 100;
    public static int cooldownTimer = 1200;
    public static int cooldownThreshold;
    public static int cooldownDuration;
    public static int powerMultiplier;
    public static int effectRingTransfer;
    public static int effectRingCapacity;

    public ItemRingEffect(int cooldownThreshold, int cooldownDuration, int powerMultiplier, int effectRingTransfer, int effectRingCapacity) {
        super(RedstoneRepository.MODID);
        globalMap = new ConcurrentHashMap<>();
        removalTimer = cooldownThreshold;
        cooldownTimer = cooldownDuration;
        maxEnergy = effectRingCapacity;
        maxTransfer = effectRingTransfer;
        energyPerUse = 2000 * powerMultiplier;
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.redstonerepository.ring.effect.title"));
        if (isActive(stack)) {
            tooltip.add(StringHelper.localizeFormat("info.redstonerepository.tooltip.active", StringHelper.BRIGHT_GREEN, StringHelper.END, StringHelper.getKeyName(KeyBindingItemMultiMode.INSTANCE.getKey())));
        } else {
            tooltip.add(StringHelper.localizeFormat("info.redstonerepository.tooltip.disabled", StringHelper.LIGHT_RED, StringHelper.END, StringHelper.getKeyName(KeyBindingItemMultiMode.INSTANCE.getKey())));
        }
        if (stack.getTagCompound() != null && stack.getTagCompound().getInteger(ON_COOLDOWN) > 0) {
            tooltip.add(StringHelper.RED + StringHelper.localizeFormat("info.redstonerepository.ring.effect.disabled", StringHelper.formatNumber((stack.getTagCompound().getInteger(ON_COOLDOWN) / 20) + 1)));
        }
        if (!RedstoneRepositoryEquipment.EquipmentInit.enable[2]) {
            tooltip.add(StringHelper.RED + "Baubles not loaded: Recipe disabled.");
        }
        tooltip.add(StringHelper.localize("info.cofh.charge") + ": " + StringHelper.getScaledNumber(getEnergyStored(stack)) + " / " + StringHelper.getScaledNumber(getMaxEnergyStored(stack)) + " RF");
        tooltip.add(StringHelper.localize("info.cofh.send") + "/" + StringHelper.localize("info.cofh.receive") + ": " + StringHelper.formatNumber(maxTransfer) + "/" + StringHelper.formatNumber(maxTransfer) + " RF/t");
    }

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Optional.Method(modid = "baubles")
    public void onEquipped(ItemStack ring, EntityLivingBase player) {
        if (!(player instanceof EntityPlayer) || player.world.isRemote || CoreUtils.isFakePlayer(player)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer) player;
        if (ring.getTagCompound() != null && !ring.getTagCompound().hasKey(ON_COOLDOWN)) {
            ring.getTagCompound().setInteger(ON_COOLDOWN, 0);
        }
        if (isActive(ring) && (getEnergyStored(ring) >= getEnergyPerUse(ring)) && ring.getTagCompound().getInteger(ON_COOLDOWN) == 0) {
            ArrayList<PotionEffect> effects = new ArrayList<>(10);
            // Use basic energy level when ring is active
            int powerUsage = getEnergyPerUse(ring);
            for (PotionEffect p: player.getActivePotionEffects()) {
                // TODO: fix access transformers
                //p.duration = POTION_DURATION_TICKS;
                effects.add(p);
                // Add to power usage per tick per potion and level (int)Math.pow(2, diff + 6.0)
                powerUsage += (int)Math.pow(getEnergyPerUse(ring), p.getAmplifier() );
            }
            // Write potion list to NBT
            writePotionEffectsToNBT(effects, ring);
            // Update global potion map and power usage
            globalMap.put(entityPlayer.getUniqueID(), effects);
            ring.getTagCompound().setInteger(POWER_TICK, powerUsage);
            ring.getTagCompound().setInteger(UNTIL_SAFE_TO_REMOVE, removalTimer);
            // Kill the old potions.
            entityPlayer.clearActivePotions();
        }
    }

    @Optional.Method(modid = "baubles")
    public void onUnequipped(ItemStack ring, EntityLivingBase player) {
        if (!(player instanceof EntityPlayer) || player.world.isRemote || CoreUtils.isFakePlayer(player)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer) player;
        if (isActive(ring) && (getEnergyStored(ring) >= getEnergyPerUse(ring))) {
            entityPlayer.clearActivePotions();
            if (ring.getTagCompound() != null) {
                if (ring.getTagCompound().getInteger(UNTIL_SAFE_TO_REMOVE) > 0 && ring.getTagCompound().getInteger(ON_COOLDOWN) == 0) {
                    ring.getTagCompound().setInteger(ON_COOLDOWN, cooldownTimer);
                }
                ring.getTagCompound().setInteger(UNTIL_SAFE_TO_REMOVE, 0);
            }
        }
        globalMap.remove(player.getUniqueID());
    }

    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack ring, EntityLivingBase player) {
        if (!(player instanceof EntityPlayer) || player.world.isRemote || CoreUtils.isFakePlayer(player)) {
            return;
        }
        if (ring.getTagCompound() != null) {
            // Somehow has been equipped without calling onEquipped, Stasis Ring has Invalid NBT! This is a bug!
            return;
        } else {
            if (ring.getTagCompound().getInteger(POWER_TICK) > maxTransfer) {
                ring.getTagCompound().setInteger(ON_COOLDOWN, cooldownTimer);
                return;
            }
            if (ring.getTagCompound().getInteger(ON_COOLDOWN) > 0) {
                ring.getTagCompound().setInteger(ON_COOLDOWN, ring.getTagCompound().getInteger(ON_COOLDOWN) - 1);
                return;
            }
        }

        EntityPlayer entityPlayer = (EntityPlayer) player;

        // Read potion list from cache
        ArrayList<PotionEffect> cacheEffects =  globalMap.get(entityPlayer.getUniqueID());
        if (cacheEffects == null && ring.getTagCompound().hasKey(EFFECTS)) {
            // Try to load potion list from NBT
            cacheEffects = readPotionEffectsFromNBT(ring.getTagCompound());
            globalMap.put(entityPlayer.getUniqueID(), cacheEffects);
        }
        if (isActive(ring) && (getEnergyStored(ring) >= ring.getTagCompound().getInteger(POWER_TICK))) {
            for (PotionEffect p : globalMap.get(entityPlayer.getUniqueID())) {
                player.addPotionEffect(p);
            }
            // Use energy to sustain potions
            useEnergyExact(ring, ring.getTagCompound().getInteger(POWER_TICK), false);
            ring.getTagCompound().setInteger(UNTIL_SAFE_TO_REMOVE, ring.getTagCompound().getInteger(UNTIL_SAFE_TO_REMOVE) - 1);
        } else {
            entityPlayer.clearActivePotions();
            globalMap.remove(player.getUniqueID());
        }
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity player, int itemSlot, boolean isSelected) {
        if (!(player instanceof EntityPlayer) || player.world.isRemote || CoreUtils.isFakePlayer(player)) {
            return;
        }
        if (stack.getTagCompound() != null && stack.getTagCompound().getInteger(ON_COOLDOWN) > 0) {
            stack.getTagCompound().setInteger(ON_COOLDOWN, stack.getTagCompound().getInteger(ON_COOLDOWN) - 1);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    public ArrayList<PotionEffect> readPotionEffectsFromNBT(NBTTagCompound tagCompound) {
        if (tagCompound == null || tagCompound.getTag(EFFECTS) == null || tagCompound.getTag(AMPLIFIER) == null) {
            return new ArrayList<>();
        }
        NBTTagList nbtEffects = (NBTTagList) tagCompound.getTag(EFFECTS);
        NBTTagList nbtAmp = (NBTTagList) tagCompound.getTag(AMPLIFIER);
        ArrayList<PotionEffect> toLoadEffects = new ArrayList<>();
        for (int i = 0; i < nbtEffects.tagCount(); i++) {
            PotionEffect p = new PotionEffect(Potion.getPotionById(nbtEffects.getIntAt(i)), POTION_DURATION_TICKS, nbtAmp.getIntAt(i));
            toLoadEffects.add(p);
        }
        return toLoadEffects;
    }

    public void writePotionEffectsToNBT(ArrayList<PotionEffect> effects, ItemStack ring) {
        NBTTagList tagListEffects = new NBTTagList();
        NBTTagList tagListIds = new NBTTagList();
        NBTTagCompound tagCompound = new NBTTagCompound();
        for (PotionEffect e : effects) {
            tagListEffects.appendTag(new NBTTagInt(e.getAmplifier()));
            tagListIds.appendTag(new NBTTagInt(Potion.getIdFromPotion(e.getPotion())));
        }
        if (!ring.hasTagCompound()) {
            tagCompound.setTag(EFFECTS, tagListIds);
            tagCompound.setTag(AMPLIFIER, tagListEffects);
            ring.setTagCompound(tagCompound);
        } else {
            ring.getTagCompound().setTag(EFFECTS, tagListIds);
            ring.getTagCompound().setTag(AMPLIFIER, tagListEffects);
        }
    }
}