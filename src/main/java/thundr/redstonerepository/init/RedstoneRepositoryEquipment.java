package thundr.redstonerepository.init;

import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.RecipeHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstonearsenal.init.RAEquipment;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.item.ItemMaterial;
import thundr.redstonerepository.item.armor.ItemArmorGelid;
import thundr.redstonerepository.item.tool.*;
import thundr.redstonerepository.item.util.ItemCapacitorAmulet;
import thundr.redstonerepository.item.util.ItemFeeder;

import java.util.Locale;

public class RedstoneRepositoryEquipment {

    public static final RedstoneRepositoryEquipment INSTANCE = new RedstoneRepositoryEquipment();

    public static ItemArmor.ArmorMaterial ARMOR_MATERIAL_GELID;
    public static Item.ToolMaterial TOOL_MATERIAL_GELID;

    private RedstoneRepositoryEquipment() {
    }

    public static void preInit() {
        for (ArmorSet e : ArmorSet.values()) {
            e.preInit();
            RedstoneRepository.PROXY.addIModelRegister(e);
        }
        for (ToolSet e : ToolSet.values()) {
            e.preInit();
            RedstoneRepository.PROXY.addIModelRegister(e);
        }
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (ArmorSet e : ArmorSet.values()) {
            e.initialize();
        }
        for (ToolSet e : ToolSet.values()) {
            e.initialize();
        }
    }

    static {
        String category;
        String comment;

        int helmet = 3;
        int chestplate = 8;
        int leggings = 6;
        int boots = 3;

        int harvestLevel = 4;
        float efficiency = 8.0F;

        category = "Equipment.Armor";
        comment = "Adjust this value to set the default protection provided by the Gelid Enderium Helmet.";
        helmet = RedstoneRepository.CONFIG_COMMON.getConfiguration().getInt("HelmetProtection", category, helmet, 1, 10, comment);

        comment = "Adjust this value to set the default protection provided by the Gelid Enderium Chestplate.";
        chestplate = RedstoneRepository.CONFIG_COMMON.getConfiguration().getInt("ChestplateProtection", category, chestplate, 1, 10, comment);

        comment = "Adjust this value to set the default protection provided by the Gelid Enderium Leggings.";
        leggings = RedstoneRepository.CONFIG_COMMON.getConfiguration().getInt("LeggingsProtection", category, leggings, 1, 10, comment);

        comment = "Adjust this value to set the default protection provided by the Gelid Enderium Boots.";
        boots = RedstoneRepository.CONFIG_COMMON.getConfiguration().getInt("BootsProtection", category, boots, 1, 10, comment);

        category = "Equipment.Tools";
        comment = "Adjust this value to set the default harvest level of Gelid Enderium Tools.";
        harvestLevel = RedstoneRepository.CONFIG_COMMON.getConfiguration().getInt("HarvestLevel", category, harvestLevel, 0, 10, comment);

        comment = "Adjust this value to set the default efficiency (mining speed) of Gelid Enderium Tools";
        efficiency = RedstoneRepository.CONFIG_COMMON.getConfiguration().getFloat("Efficiency", category, efficiency, 2.0F, 32.0F, comment);

        ARMOR_MATERIAL_GELID = EnumHelper.addArmorMaterial("RR:GELIDENDERIUM", "gelid_armor", 100, new int[] { boots, leggings, chestplate, helmet }, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
        TOOL_MATERIAL_GELID = EnumHelper.addToolMaterial("RR:GELIDENDERIUM", harvestLevel, 100, efficiency, 0, 25);
    }

    public enum ArmorSet implements IModelRegister {

        GELID("gelid", RedstoneRepositoryEquipment.ARMOR_MATERIAL_GELID);

        private final String name;
        private final ItemArmor.ArmorMaterial ARMOR_MATERIAL;

        public ItemArmorGelid itemHelmet;
        public ItemArmorGelid itemChestplate;
        public ItemArmorGelid itemLeggings;
        public ItemArmorGelid itemBoots;

        public ItemStack armorHelmet;
        public ItemStack armorChestplate;
        public ItemStack armorLeggings;
        public ItemStack armorBoots;

        public boolean[] enable = new boolean[4];

        ArmorSet(String name, ItemArmor.ArmorMaterial material) {
            this.name = name.toLowerCase(Locale.US);
            this.ARMOR_MATERIAL = material;
        }

        protected void create() {
            this.itemHelmet = new ItemArmorGelid(this.ARMOR_MATERIAL, EntityEquipmentSlot.HEAD);
            this.itemChestplate = new ItemArmorGelid(this.ARMOR_MATERIAL, EntityEquipmentSlot.CHEST);
            this.itemLeggings = new ItemArmorGelid(this.ARMOR_MATERIAL, EntityEquipmentSlot.LEGS);
            this.itemBoots = new ItemArmorGelid(this.ARMOR_MATERIAL, EntityEquipmentSlot.FEET);
        }

        protected void preInit() {
            final String ARMOR = "redstonerepository.armor." + this.name;
            final String PATH_ARMOR = "redstonerepository:textures/models/armor/";
            final String[] TEXTURE = { PATH_ARMOR + this.name + "_1.png", PATH_ARMOR + this.name + "_2.png" };

            String category = "Equipment.Armor." + StringHelper.titleCase(this.name);

            this.enable[0] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Helmet", true).getBoolean(true);
            this.enable[1] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Chestplate", true).getBoolean(true);
            this.enable[2] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Leggings", true).getBoolean(true);
            this.enable[3] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Boots", true).getBoolean(true);
            
            this.create();
            
            this.itemHelmet.setArmorTextures(TEXTURE).setUnlocalizedName(ARMOR + "Helmet").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemHelmet.setShowInCreative(this.enable[0]);
            this.itemHelmet.setRegistryName("armor.helmet_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemHelmet);

            this.itemChestplate.setArmorTextures(TEXTURE).setUnlocalizedName(ARMOR + "Chestplate").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemChestplate.setShowInCreative(this.enable[1]);
            this.itemChestplate.setRegistryName("armor.chestplate" + this.name);
            ForgeRegistries.ITEMS.register(this.itemChestplate);

            this.itemLeggings.setArmorTextures(TEXTURE).setUnlocalizedName(ARMOR + "Leggings").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemLeggings.setShowInCreative(this.enable[2]);
            this.itemLeggings.setRegistryName("armor.leggings_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemLeggings);

            this.itemBoots.setArmorTextures(TEXTURE).setUnlocalizedName(ARMOR + "Boots").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemBoots.setShowInCreative(this.enable[3]);
            this.itemBoots.setRegistryName("armor.boots_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemBoots);

            this.armorHelmet = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemHelmet), 0);
            this.armorChestplate = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemChestplate), 0);
            this.armorLeggings = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemLeggings), 0);
            this.armorBoots = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemBoots), 0);
        }

        protected void initialize() {
            if (this.enable[0]) {
                RecipeHelper.addShapedRecipe(this.armorHelmet, "III", "IAI", 'I', ItemMaterial.plateArmorGelidEnderium, 'A', RAEquipment.ArmorSet.FLUX.itemHelmet);
            }
            if (this.enable[1]) {
                RecipeHelper.addShapedRecipe(this.armorChestplate, "IAI", "III", "III", 'I', ItemMaterial.plateArmorGelidEnderium, 'A', RAEquipment.ArmorSet.FLUX.itemPlate);
            }
            if (this.enable[2]) {
                RecipeHelper.addShapedRecipe(this.armorLeggings, "III", "IAI", "I I", 'I', ItemMaterial.plateArmorGelidEnderium, 'A', RAEquipment.ArmorSet.FLUX.itemLegs);
            }
            if (this.enable[3]) {
                RecipeHelper.addShapedRecipe(this.armorBoots, "IAI", "I I", 'I', ItemMaterial.plateArmorGelidEnderium, 'A', RAEquipment.ArmorSet.FLUX.itemBoots);
            }
        }

        @SideOnly(Side.CLIENT)
        public void registerModel(Item item, String stackName) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(RedstoneRepository.MODID, "armor"), "type=" + stackName));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerModels() {
            this.registerModel(this.itemHelmet, "helmet_" + this.name);
            this.registerModel(this.itemChestplate, "chestplate_" + this.name);
            this.registerModel(this.itemLeggings, "leggings_" + this.name);
            this.registerModel(this.itemBoots, "boots_" + this.name);
        }
    }

    public enum ToolSet implements IModelRegister {
        GELID("gelid", RedstoneRepositoryEquipment.TOOL_MATERIAL_GELID);

        private final String name;
        private final Item.ToolMaterial TOOL_MATERIAL;

        public ItemBattleWrenchGelid itemBattleWrench;
        public ItemSwordGelid itemSword;
        public ItemShovelGelid itemShovel;
        public ItemPickaxeGelid itemPickaxe;
        public ItemAxeGelid itemAxe;
        public ItemSickleGelid itemSickle;
        public ItemBowGelid itemBow;
        public ItemShieldGelid itemShield;
        public ItemHammerGelid itemHammer;
        public ItemExcavatorGelid itemExcavator;
        public ItemFishingRodGelid itemFishingRod;
        public ItemWrenchGelid itemWrench;

        public ItemStack toolBattleWrench;
        public ItemStack toolSword;
        public ItemStack toolShovel;
        public ItemStack toolPickaxe;
        public ItemStack toolAxe;
        public ItemStack toolSickle;
        public ItemStack toolBow;
        public ItemStack toolShield;
        public ItemStack toolHammer;
        public ItemStack toolExcavator;
        public ItemStack toolFishingRod;
        public ItemStack toolWrench;

        private float arrowDamage = 0.5F;
        private float arrowSpeed = 0.5F;
        private float zoomMultiplier = 0.35F;
        private int luckModifier = 1;
        private int speedModifier = 1;

        public int axeBlocksCutPerTick;

        public boolean[] enable = new boolean[12];

        ToolSet(String name, Item.ToolMaterial material) {
            this.name = name.toLowerCase(Locale.US);
            this.TOOL_MATERIAL = material;
        }

        protected void create() {
            this.itemBattleWrench = new ItemBattleWrenchGelid(this.TOOL_MATERIAL);
            this.itemSword = new ItemSwordGelid(this.TOOL_MATERIAL);
            this.itemShovel = new ItemShovelGelid(this.TOOL_MATERIAL);
            this.itemPickaxe = new ItemPickaxeGelid(this.TOOL_MATERIAL);
            this.itemAxe = new ItemAxeGelid(this.TOOL_MATERIAL, this.axeBlocksCutPerTick);
            this.itemSickle = new ItemSickleGelid(this.TOOL_MATERIAL);
            this.itemBow = new ItemBowGelid(this.TOOL_MATERIAL);
            this.itemShield = new ItemShieldGelid(this.TOOL_MATERIAL);
            this.itemHammer = new ItemHammerGelid(this.TOOL_MATERIAL);
            this.itemExcavator = new ItemExcavatorGelid(this.TOOL_MATERIAL);
            this.itemFishingRod = new ItemFishingRodGelid(this.TOOL_MATERIAL);
            this.itemWrench = new ItemWrenchGelid(this.TOOL_MATERIAL);
        }

        protected void config() {
            this.axeBlocksCutPerTick = RedstoneRepository.CONFIG_COMMON.getConfiguration().get("Equipment.Tools.Axe", "BlocksPerTick", 3, "Sets the number of blocks per tick the axe attempts to cut in empowered mode. Higher values cause more lag. ").setMinValue(0).setMaxValue(10).getInt();
        }

        protected void preInit() {

            final String TOOL = "redstonerepository.tool." + this.name;

            String category = "Equipment.Tools." + StringHelper.titleCase(this.name);

            this.enable[0] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "BattleWrench", true).getBoolean(true);
            this.enable[1] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Sword", true).getBoolean(true);
            this.enable[2] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Shovel", true).getBoolean(true);
            this.enable[3] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Pickaxe", true).getBoolean(true);
            this.enable[4] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Axe", true).getBoolean(true);
            this.enable[5] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Sickle", true).getBoolean(true);
            this.enable[6] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Bow", true).getBoolean(true);
            this.enable[7] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Shield", true).getBoolean(true);
            this.enable[8] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Hammer", true).getBoolean(true);
            this.enable[9] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Excavator", true).getBoolean(true);
            this.enable[10] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "FishingRod", true).getBoolean(true);
            this.enable[11] = RedstoneRepository.CONFIG_COMMON.getConfiguration().get(category, "Wrench", true).getBoolean(true);

            this.config();
            this.create();

            this.itemBattleWrench.setUnlocalizedName(TOOL + "BattleWrench").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemBattleWrench.setShowInCreative(this.enable[0]);
            this.itemBattleWrench.setRegistryName("tool.battlewrench_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemBattleWrench);

            this.itemSword.setUnlocalizedName(TOOL + "Sword").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemSword.setShowInCreative(this.enable[1]);
            this.itemSword.setRegistryName("tool.sword_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemSword);

            this.itemShovel.setUnlocalizedName(TOOL + "Shovel").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemShovel.setShowInCreative(this.enable[2]);
            this.itemShovel.setRegistryName("tool.shovel_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemShovel);

            this.itemPickaxe.setUnlocalizedName(TOOL + "Pickaxe").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemPickaxe.setShowInCreative(this.enable[3]);
            this.itemPickaxe.setRegistryName("tool.pickaxe_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemPickaxe);

            this.itemAxe.setUnlocalizedName(TOOL + "Axe").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemAxe.setShowInCreative(this.enable[4]);
            this.itemAxe.setRegistryName("tool.axe_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemAxe);

            this.itemSickle.setUnlocalizedName(TOOL + "Sickle").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemSickle.setShowInCreative(this.enable[5]);
            this.itemSickle.setRegistryName("tool.sickle_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemSickle);

            this.itemBow.setUnlocalizedName(TOOL + "Bow").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemBow.setShowInCreative(this.enable[6]);
            this.itemBow.setRegistryName("tool.bow_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemBow);

            this.itemShield.setUnlocalizedName(TOOL + "Shield").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemShield.setShowInCreative(this.enable[7]);
            this.itemShield.setRegistryName("tool.shield_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemShield);

            this.itemHammer.setUnlocalizedName(TOOL + "Hammer").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemHammer.setShowInCreative(this.enable[8]);
            this.itemHammer.setRegistryName("tool.hammer_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemHammer);

            this.itemExcavator.setUnlocalizedName(TOOL + "Excavator").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemExcavator.setShowInCreative(this.enable[9]);
            this.itemExcavator.setRegistryName("tool.excavator_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemExcavator);

            this.itemFishingRod.setUnlocalizedName(TOOL + "FishingRod").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemFishingRod.setShowInCreative(this.enable[10]);
            this.itemFishingRod.setRegistryName("tool.fishing_rod_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemFishingRod);

            this.itemWrench.setUnlocalizedName(TOOL + "Wrench").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemWrench.setShowInCreative(this.enable[11]);
            this.itemWrench.setRegistryName("tool.wrench_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemWrench);

            this.toolBattleWrench = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemBattleWrench), 0);
            this.toolSword = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemSword), 0);
            this.toolShovel = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemShovel), 0);
            this.toolPickaxe = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemPickaxe), 0);
            this.toolAxe = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemAxe), 0);
            this.toolSickle = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemSickle), 0);
            this.toolBow = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemBow), 0);
            this.toolShield = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemShield), 0);
            this.toolHammer = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemHammer), 0);
            this.toolExcavator = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemExcavator), 0);
            this.toolFishingRod = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemFishingRod), 0);
            this.toolWrench = EnergyHelper.setDefaultEnergyTag(new ItemStack(this.itemWrench), 0);
        }

        protected void initialize() {
            if (this.enable[0]) {
                RecipeHelper.addShapedRecipe(this.toolBattleWrench, "IWI", " G ", " R ", 'I', "ingotGelidEnderium", 'G', "gearGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'W', RAEquipment.ToolSet.FLUX.itemBattleWrench);
            }
            if (this.enable[1]) {
                RecipeHelper.addShapedRecipe(this.toolSword, " I ", " S ", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemSword);
            }
            if (this.enable[2]) {
                RecipeHelper.addShapedRecipe(this.toolShovel, " I ", " S ", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemShovel);
            }
            if (this.enable[3]) {
                RecipeHelper.addShapedRecipe(this.toolPickaxe, "III", " P ", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'P', RAEquipment.ToolSet.FLUX.itemPickaxe);
            }
            if (this.enable[4]) {
                RecipeHelper.addShapedRecipe(this.toolAxe, "II ", "IA ", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'A', RAEquipment.ToolSet.FLUX.itemAxe);
            }
            if (this.enable[5]) {
                RecipeHelper.addShapedRecipe(this.toolSickle, " I ", " SI", "RI ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemSickle);
            }
            if (this.enable[6]) {
                RecipeHelper.addShapedRecipe(this.toolBow, " I ", "RS ", " I ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemBow);
            }
            if (this.enable[7]) {
                RecipeHelper.addShapedRecipe(this.toolShield, "IGI", "ISI", " I ", 'I', "ingotGelidEnderium", 'G', ItemMaterial.gemGelidCrystal, 'S', RAEquipment.ToolSet.FLUX.itemShield);
            }
            if (this.enable[8]) {
                RecipeHelper.addShapedRecipe(this.toolHammer, "III", "ISI", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemHammer);
            }
            if (this.enable[9]) {
                RecipeHelper.addShapedRecipe(this.toolExcavator, " I ", "ISI", " R ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemExcavator);
            }
            if (this.enable[10]) {
                RecipeHelper.addShapedRecipe(this.toolFishingRod, "  I", " S ", "R  ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemFishingRod);
            }
            if (this.enable[11]) {
                RecipeHelper.addShapedRecipe(this.toolWrench, "ISI", " R ", " I ", 'I', "ingotGelidEnderium", 'R', ItemMaterial.rodGelidObsidian, 'S', RAEquipment.ToolSet.FLUX.itemWrench);
            }
        }

        @SideOnly(Side.CLIENT)
        public void registerModel(Item item, String stackName) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(RedstoneRepository.MODID, "tool/" + stackName), "inventory"));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void registerModels() {
            this.registerModel(this.itemBattleWrench, "battle_wrench_" + this.name);
            this.registerModel(this.itemSword, "sword_" + this.name);
            this.registerModel(this.itemShovel, "shovel_" + this.name);
            this.registerModel(this.itemPickaxe, "pickaxe_" + this.name);
            this.registerModel(this.itemAxe, "axe_" + this.name);
            this.registerModel(this.itemSickle, "sickle_" + this.name);
            this.registerModel(itemBow, "bow_" + name);
            this.registerModel(itemShield, "shield_" + name);
            this.registerModel(itemHammer, "hammer_" + name);
            this.registerModel(itemExcavator, "excavator_" + name);
            this.registerModel(itemFishingRod, "fishing_rod_" + name);
            this.registerModel(itemWrench, "wrench_" + name);
        }
    }

    public static class EquipmentInit implements IInitializer, IModelRegister {

        @GameRegistry.ItemStackHolder(value = "thermalexpansion:capacitor", meta = 4)
        public static final ItemStack resonantCapacitor;
        @GameRegistry.ItemStackHolder(value = "thermalexpansion:capacitor", meta = 1)
        public static final ItemStack hardenedCapacitor;

        public static ItemFeeder itemFeeder;
        public static ItemCapacitorAmulet itemCapacitorAmulet;
        public static ItemStack capacitorAmuletGelid;
        public static ItemStack feederStack;
        public static ItemStack mushroomStewBucket;

        public static boolean[] enable;
        public static int capacity;
        public static int transfer;
        public static int hungerPointsMax;
        public static int feederCapacity;
        public static int feederMaxTransfer;
        public static int feederEnergyPerUse;
        public static int feederMaxSat;

        static {
            resonantCapacitor = null;
            hardenedCapacitor = null;
            enable = new boolean[2];
        }

        public boolean preInit() {
            this.config();
            itemCapacitorAmulet = new ItemCapacitorAmulet(capacity, transfer);
            itemCapacitorAmulet.setUnlocalizedName("redstonerepository.util.gelidCapacitor").setCreativeTab(RedstoneRepository.tabCommon);
            itemCapacitorAmulet.setRegistryName("util.capacitor_gelid");
            ForgeRegistries.ITEMS.register(itemCapacitorAmulet);
            capacitorAmuletGelid = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemCapacitorAmulet), 0);
            itemFeeder = new ItemFeeder(hungerPointsMax, feederCapacity, feederMaxTransfer, feederEnergyPerUse, feederMaxSat);
            itemFeeder.setUnlocalizedName("redstonerepository.util.feeder").setCreativeTab(RedstoneRepository.tabCommon);
            itemFeeder.setRegistryName("util.feeder");
            ForgeRegistries.ITEMS.register(itemFeeder);
            feederStack = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemFeeder), 0);
            RedstoneRepository.PROXY.addIModelRegister(this);
            return true;
        }

        public void config() {
            boolean enableConfig = RedstoneRepository.CONFIG_COMMON.get("Item.Capacitor", "Enable", true, "Enable the Gelid Capacitor Amulet");
            boolean enableLoaded = Loader.isModLoaded("baubles");
            EquipmentInit.enable[0] = enableConfig && enableLoaded;
            transfer = RedstoneRepository.CONFIG_COMMON.get("Item.Capacitor", "BaseTransfer", 100000, "Set the base transfer rate of the Gelid Capacitor Amulet in RF/t (Default 100,000) ");
            capacity = RedstoneRepository.CONFIG_COMMON.get("Item.Capacitor", "BaseCapacity", 100000000, "Set the base capacity of the Gelid Capacitor Amulet in RF/t (Default 100,000,000) ");
            boolean enableFeederConfig = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "Enable", true, "Enable the Endoscopic Gastrostomizer (Automatic Feeder)");
            EquipmentInit.enable[1] = enableFeederConfig && enableLoaded;
            hungerPointsMax = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "MaxHungerPoints", 500, "Set the maximum hunger point storage of the Endoscopic Gastrostomizer (EG) (Default 500)");
            feederCapacity = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "BaseCapacity", 4000000, "Set the base capacity of the E.G. in RF (Default 4,000,000) ");
            feederMaxTransfer = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "MaxTransfer", 8000, "Set the maximum transfer rate into the item in RF/t (Default 8000)");
            feederEnergyPerUse = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "EnergyPerUse", 30000, "Set amount of energy used per food point in RF (Default 3000)");
            feederMaxSat = RedstoneRepository.CONFIG_COMMON.get("Item.Feeder", "SaturationFillLevel", 5, "Maximum amount of hunger saturation to automatically fill to. Higher numbers consume hunger points more quickly. (Default 5, Max 20)");
        }

        public boolean initialize() {
            mushroomStewBucket = FluidUtil.getFilledBucket(FluidRegistry.getFluidStack("mushroom_stew", 1000));
            if (enable[0]) {
                RecipeHelper.addShapedRecipe(capacitorAmuletGelid, " S ", "ACA", "AGA", 'S', ItemMaterial.stringFluxed, 'A', ItemMaterial.plateArmorGelidEnderium, 'G', ItemMaterial.ingotGelidEnderium, 'C', resonantCapacitor);
            }
            if (enable[1]) {
                RecipeHelper.addShapedRecipe(feederStack, "SCS", "PMP", " G ", 'S', ItemMaterial.stringFluxed, 'C', hardenedCapacitor, 'M', mushroomStewBucket, 'P', ItemMaterial.plateGelidEnderium, 'G', ItemMaterial.gearGelidEnderium);
            }
            return true;
        }

        @SideOnly(Side.CLIENT)
        public void registerModels() {
            ModelLoader.setCustomModelResourceLocation(itemCapacitorAmulet, 0, new ModelResourceLocation(new ResourceLocation(RedstoneRepository.MODID, "util/capacitor_gelid"), "inventory"));
            ModelLoader.setCustomModelResourceLocation(itemFeeder, 0, new ModelResourceLocation(new ResourceLocation(RedstoneRepository.MODID, "util/feeder"), "inventory"));
        }
    }

}

