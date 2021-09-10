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
    public static final RegistryObject<RRItem> GEAR_GELID_ENDERIUM = ITEMS.register("gear_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> INGOT_GELID_ENDERIUM = ITEMS.register("ingot_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> PLATE_GELID_ENDERIUM = ITEMS.register("plate_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> NUGGET_GELID_ENDERIUM = ITEMS.register("nugget_gelid_enderium", RRItem::new);
    public static final RegistryObject<RRItem> ARMORPLATING_GELID_ENDERIUM = ITEMS.register("armorplating_gelid_enderium", RRItem::new);

    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_HELMET = ITEMS.register("gelid_enderium_helmet", () -> new GelidEnderiumArmor(EquipmentSlotType.HEAD));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_CHESTPLATE = ITEMS.register("gelid_enderium_chestplate", () -> new GelidEnderiumArmor(EquipmentSlotType.CHEST));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_LEGGINGS = ITEMS.register("gelid_enderium_leggings", () -> new GelidEnderiumArmor(EquipmentSlotType.LEGS));
    public static final RegistryObject<GelidEnderiumArmor> GELID_ENDERIUM_BOOTS = ITEMS.register("gelid_enderium_boots", () -> new GelidEnderiumArmor(EquipmentSlotType.FEET));


}
