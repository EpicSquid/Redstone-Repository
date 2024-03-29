package thundr.redstonerepository.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.api.IArmorEnderium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ArmorEventHandler {

    public static int fallDrainFactor;
    public static int fireDrainFactor;
    public static int fluxDrainFactor;

    public static void preInit() {
        fallDrainFactor = RedstoneRepository.CONFIG_COMMON.get("Equipment.Armor", "FallDamageDrainFactor", 2400, "Set the factor that calculates how much energy a fall drains. Scales with height.");
        fireDrainFactor = RedstoneRepository.CONFIG_COMMON.get("Equipment.Armor", "FireDamageDrainFactor", 1200, "Set the factor that calculates how much energy fire-type damage drains.");
        fluxDrainFactor = RedstoneRepository.CONFIG_COMMON.get("Equipment.Armor", "FluxDamageDrainFactor", 4800, "Set the factor that calculates how much energy Flux damage drains.");
    }

    @SubscribeEvent
    public void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            ArmorSummary summary;
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!player.world.isRemote && (summary = new ArmorSummary().getSummary(player)) != null && summary.enderiumPieces.containsKey("Boots")) {
                int toDrain = (int) (event.getDistance() * (float) fallDrainFactor);
                if (summary.energyStored.get("Boots") >= toDrain) {
                    ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                    event.setDamageMultiplier(0.0f);
                    summary.enderiumPieces.get("Boots").extractEnergy(boots, toDrain, false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttacked(LivingAttackEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!player.world.isRemote) {
                ArmorSummary summary = new ArmorSummary().getSummary(player);
                if (summary.isFullSet) {
                    if (event.getSource().isFireDamage()) {
                        if (this.doFullArmorDrain((int) event.getAmount() * fireDrainFactor, summary, player)) {
                            event.setCanceled(true);
                        }
                    } else if (event.getSource().getDamageType().contains("flux") && this.doFullArmorDrain((int) event.getAmount() * fluxDrainFactor, summary, player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private boolean doFullArmorDrain(int toDrain, ArmorSummary summary, EntityPlayer player) {
        if (summary.totalEnergyStored >= toDrain) {
            Iterator<ItemStack> armor = player.getArmorInventoryList().iterator();
            summary.enderiumPieces.forEach((key, value) -> value.extractEnergy(armor.next(), toDrain / 4, false));
            return true;
        }
        return false;
    }

    public static class ArmorSummary {
        private final ArrayList<ItemStack> armorStacks = new ArrayList<>();
        public LinkedHashMap<String, Integer> energyStored = new LinkedHashMap<>();
        public int totalEnergyStored = 0;
        public LinkedHashMap<String, IArmorEnderium> enderiumPieces = new LinkedHashMap<>();
        public boolean isFullSet = false;

        public ArmorSummary getSummary(EntityPlayer player) {
            this.energyStored.put("Boots", 0);
            this.energyStored.put("Leggings", 0);
            this.energyStored.put("Chestplate", 0);
            this.energyStored.put("Helmet", 0);
            this.armorStacks.add(player.getItemStackFromSlot(EntityEquipmentSlot.FEET));
            this.armorStacks.add(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
            this.armorStacks.add(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
            this.armorStacks.add(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
            int iter = 0;
            for (ItemStack i : this.armorStacks) {
                if (i.isEmpty() || !(i.getItem() instanceof IArmorEnderium)) {
                    ++iter;
                    continue;
                }
                IArmorEnderium armor = (IArmorEnderium) i.getItem();
                int energy = armor.getEnergyStored(i);
                switch (iter) {
                    case 0: {
                        this.energyStored.replace("Boots", 0, energy);
                        this.totalEnergyStored += energy;
                        this.enderiumPieces.put("Boots", armor);
                        break;
                    }
                    case 1: {
                        this.energyStored.replace("Leggings", 0, energy);
                        this.totalEnergyStored += energy;
                        this.enderiumPieces.put("Leggings", armor);
                        break;
                    }
                    case 2: {
                        this.energyStored.replace("Chestplate", 0, energy);
                        this.totalEnergyStored += energy;
                        this.enderiumPieces.put("Chestplate", armor);
                        break;
                    }
                    case 3: {
                        this.energyStored.replace("Helmet", 0, energy);
                        this.totalEnergyStored += energy;
                        this.enderiumPieces.put("Helmet", armor);
                        break;
                    }
                }
                ++iter;
            }
            if (this.enderiumPieces == null) {
                return null;
            }
            this.isFullSet = this.enderiumPieces.size() == 4;
            return this;
        }
    }

}

