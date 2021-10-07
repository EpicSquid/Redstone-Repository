package thundr.redstonerepository.handlers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.item.GelidEnderiumArmor;
import thundr.redstonerepository.item.RRItem;

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
    public static final RegistryObject<RRItem> STRING_FLUXED = ITEMS.register("string_fluxed", RRItem::new);
    public static final RegistryObject<RRItem> ROD_GELID = ITEMS.register("rod_gelid", RRItem::new);
    public static final RegistryObject<RRItem> GEM_GELID = ITEMS.register("gem_gelid", RRItem::new);
    public static final RegistryObject<RRItem> DUST_GELID_ENDERIUM = ITEMS.register("dust_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> COIN_GELID_ENDERIUM = ITEMS.register("coin_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> GEAR_GELID_ENDERIUM = ITEMS.register("gear_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> INGOT_GELID_ENDERIUM = ITEMS.register("ingot_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> PLATE_GELID_ENDERIUM = ITEMS.register("plate_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> NUGGET_GELID_ENDERIUM = ITEMS.register("nugget_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> ARMORPLATING_GELID_ENDERIUM = ITEMS.register("armorplating_gelid_enderium", RRItem::new);

    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_HELMET = ITEMS.register("gelid_enderium_helmet", () -> new GelidEnderiumArmor(EquipmentSlotType.HEAD));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_CHESTPLATE = ITEMS.register("gelid_enderium_chestplate", () -> new GelidEnderiumArmor(EquipmentSlotType.CHEST));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_LEGGINGS = ITEMS.register("gelid_enderium_leggings", () -> new GelidEnderiumArmor(EquipmentSlotType.LEGS));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_BOOTS = ITEMS.register("gelid_enderium_boots", () -> new GelidEnderiumArmor(EquipmentSlotType.FEET));

    public static final RegistryObject<RRItem> AXE_GELID = ITEMS.register("axe_gelid", RRItem::new);
    public static final RegistryObject<RRItem> BATTLE_WRENCH_GELID = ITEMS.register("battle_wrench_gelid", RRItem::new);
    public static final RegistryObject<RRItem> BOW_GELID = ITEMS.register("bow_gelid", RRItem::new);
    public static final RegistryObject<RRItem> EXCAVATOR_GELID = ITEMS.register("excavator_gelid", RRItem::new);
    public static final RegistryObject<RRItem> FISHING_ROD_GELID = ITEMS.register("fishing_rod_gelid", RRItem::new);
    public static final RegistryObject<RRItem> HAMMER_GELID = ITEMS.register("hammer_gelid", RRItem::new);
    public static final RegistryObject<RRItem> PICKAXE_GELID = ITEMS.register("pickaxe_gelid", RRItem::new);
    public static final RegistryObject<RRItem> SHIELD_GELID = ITEMS.register("shield_gelid", RRItem::new);
    public static final RegistryObject<RRItem> SHOVEL_GELID = ITEMS.register("shovel_gelid", RRItem::new);
    public static final RegistryObject<RRItem> SICKLE_GELID = ITEMS.register("sickle_gelid", RRItem::new);
    public static final RegistryObject<RRItem> SWORD_GELID = ITEMS.register("sword_gelid", RRItem::new);
    public static final RegistryObject<RRItem> WRENCH_GELID = ITEMS.register("wrench_gelid", RRItem::new);

    public static final RegistryObject<RRItem> CAPACITOR_GELID = ITEMS.register("capacitor_gelid", RRItem::new);
    public static final RegistryObject<RRItem> FEEDER = ITEMS.register("feeder", RRItem::new);
    public static final RegistryObject<RRItem> QUIVER_GELID = ITEMS.register("quiver_gelid", RRItem::new);
    public static final RegistryObject<RRItem> RING_BASE = ITEMS.register("ring_base", RRItem::new);
    public static final RegistryObject<RRItem> RING_EFFECT = ITEMS.register("ring_effect", RRItem::new);
    public static final RegistryObject<RRItem> RING_MINING = ITEMS.register("ring_mining", RRItem::new);
}
