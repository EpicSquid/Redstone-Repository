package dev.tomheaton.redstonerepository.handlers;

import dev.tomheaton.redstonerepository.RedstoneRepository;
import dev.tomheaton.redstonerepository.item.GelidEnderiumArmor;
import dev.tomheaton.redstonerepository.item.RRItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RedstoneRepository.MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, RedstoneRepository.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RedstoneRepository.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RedstoneRepository.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RedstoneRepository.MODID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Items:
    public static final RegistryObject<RRItem> GELID_COIN = ITEMS.register("gelid_coin", RRItem::new);
    public static final RegistryObject<RRItem> GELID_DUST = ITEMS.register("gelid_dust", RRItem::new);
    public static final RegistryObject<RRItem> GELID_GEAR = ITEMS.register("gelid_gear", RRItem::new);
    public static final RegistryObject<RRItem> GELID_GEM = ITEMS.register("gelid_gem", RRItem::new);
    public static final RegistryObject<RRItem> GELID_INGOT = ITEMS.register("gelid_ingot", RRItem::new);
    public static final RegistryObject<RRItem> GELID_NUGGET = ITEMS.register("gelid_nugget", RRItem::new);
    public static final RegistryObject<RRItem> GELID_PLATE = ITEMS.register("gelid_plate", RRItem::new);
    public static final RegistryObject<RRItem> GELID_PLATING = ITEMS.register("gelid_plating", RRItem::new);
    public static final RegistryObject<RRItem> FLUX_STRING = ITEMS.register("flux_string", () -> new RRItem(Rarity.UNCOMMON));
    public static final RegistryObject<RRItem> GELID_OBSIDIAN_ROD = ITEMS.register("gelid_obsidian_rod", RRItem::new);

    public static final RegistryObject<GelidEnderiumArmor> GELID_HELMET = ITEMS.register("gelid_helmet", () -> new GelidEnderiumArmor(EquipmentSlotType.HEAD));
    public static final RegistryObject<GelidEnderiumArmor> GELID_CHESTPLATE = ITEMS.register("gelid_chestplate", () -> new GelidEnderiumArmor(EquipmentSlotType.CHEST));
    public static final RegistryObject<GelidEnderiumArmor> GELID_LEGGINGS = ITEMS.register("gelid_leggings", () -> new GelidEnderiumArmor(EquipmentSlotType.LEGS));
    public static final RegistryObject<GelidEnderiumArmor> GELID_BOOTS = ITEMS.register("gelid_boots", () -> new GelidEnderiumArmor(EquipmentSlotType.FEET));
    public static final RegistryObject<RRItem> GELID_ELYTRA = ITEMS.register("gelid_elytra", RRItem::new);
    public static final RegistryObject<RRItem> GELID_ELYTRA_CONTROLLER = ITEMS.register("gelid_elytra_controller", RRItem::new);

    public static final RegistryObject<RRItem> GELID_PICKAXE = ITEMS.register("gelid_pickaxe", RRItem::new);
    public static final RegistryObject<RRItem> GELID_SHOVEL = ITEMS.register("gelid_shovel", RRItem::new);
    public static final RegistryObject<RRItem> GELID_AXE = ITEMS.register("gelid_axe", RRItem::new);
    //    public static final RegistryObject<RRItem> GELID_HOE = ITEMS.register("gelid_hoe", RRItem::new);
    public static final RegistryObject<RRItem> GELID_SICKLE = ITEMS.register("gelid_sickle", RRItem::new);
    public static final RegistryObject<RRItem> GELID_HAMMER = ITEMS.register("gelid_hammer", RRItem::new);
    public static final RegistryObject<RRItem> GELID_EXCAVATOR = ITEMS.register("gelid_excavator", RRItem::new);
    public static final RegistryObject<RRItem> GELID_WRENCH = ITEMS.register("gelid_wrench", RRItem::new);
    public static final RegistryObject<RRItem> GELID_FISHING_ROD = ITEMS.register("gelid_fishing_rod", RRItem::new);

    public static final RegistryObject<RRItem> GELID_SWORD = ITEMS.register("gelid_sword", RRItem::new);
    public static final RegistryObject<RRItem> GELID_SHIELD = ITEMS.register("gelid_shield", RRItem::new);
    public static final RegistryObject<RRItem> GELID_TRIDENT = ITEMS.register("gelid_trident", RRItem::new);
    public static final RegistryObject<RRItem> GELID_BOW = ITEMS.register("gelid_bow", RRItem::new);
    public static final RegistryObject<RRItem> GELID_CROSSBOW = ITEMS.register("gelid_crossbow", RRItem::new);
    public static final RegistryObject<RRItem> GELID_QUIVER = ITEMS.register("quiver", RRItem::new);

    public static final RegistryObject<RRItem> GELID_CAPACITOR = ITEMS.register("gelid_capacitor", RRItem::new);
    public static final RegistryObject<RRItem> GELID_FEEDER = ITEMS.register("gelid_feeder", RRItem::new);
    public static final RegistryObject<RRItem> GELID_RING_BASE = ITEMS.register("gelid_ring_base", () -> new RRItem(Rarity.EPIC));
    public static final RegistryObject<RRItem> GELID_RING_EFFECT = ITEMS.register("gelid_ring_effect", () -> new RRItem(Rarity.EPIC));
    public static final RegistryObject<RRItem> GELID_RING_MINING = ITEMS.register("gelid_ring_mining", () -> new RRItem(Rarity.EPIC));
}
