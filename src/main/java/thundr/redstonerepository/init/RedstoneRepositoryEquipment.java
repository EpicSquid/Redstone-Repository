package thundr.redstonerepository.init;

import cofh.core.item.ItemArmorCore;
import cofh.core.item.tool.ItemToolCore;
import cofh.core.render.IModelRegister;
import cofh.core.util.ConfigHandler;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.RecipeHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.redstonearsenal.init.RAEquipment;
import cofh.redstonearsenal.item.armor.ItemArmorFlux;
import cofh.redstonearsenal.item.tool.ItemAxeFlux;
import cofh.redstonearsenal.item.tool.ItemBattleWrenchFlux;
import cofh.redstonearsenal.item.tool.ItemPickaxeFlux;
import cofh.redstonearsenal.item.tool.ItemShovelFlux;
import cofh.redstonearsenal.item.tool.ItemSickleFlux;
import cofh.redstonearsenal.item.tool.ItemSwordFlux;
import java.util.Locale;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.items.ItemFeeder;
import thundr.redstonerepository.items.ItemMaterial;
import thundr.redstonerepository.items.armor.ItemArmorEnderium;
import thundr.redstonerepository.items.baubles.ItemCapacitorAmulet;
import thundr.redstonerepository.items.tools.gelidenderium.ItemAxeGelidEnderium;
import thundr.redstonerepository.items.tools.gelidenderium.ItemBattleWrenchGelidEnderium;
import thundr.redstonerepository.items.tools.gelidenderium.ItemPickaxeGelidEnderium;
import thundr.redstonerepository.items.tools.gelidenderium.ItemShovelGelidEnderium;
import thundr.redstonerepository.items.tools.gelidenderium.ItemSickleGelidEnderium;
import thundr.redstonerepository.items.tools.gelidenderium.ItemSwordGelidEnderium;
import thundr.redstonerepository.proxies.CommonProxy;

public class RedstoneRepositoryEquipment {
    public static final RedstoneRepositoryEquipment INSTANCE = new RedstoneRepositoryEquipment();
    public static EquipmentInit equipInit;
    public static final ItemArmor.ArmorMaterial ARMOR_MATERIAL_GELID;
    public static final Item.ToolMaterial TOOL_MATERIAL_GELID;

    private RedstoneRepositoryEquipment() {
    }

    public static void preInit() {
        for (ArmorSet e : ArmorSet.values()) {
            e.initialize();
            RedstoneRepository.proxy.addIModelRegister(e);
        }
        for (ToolSet e : ToolSet.values()) {
            e.initialize();
            RedstoneRepository.proxy.addIModelRegister(e);
        }
        equipInit = new EquipmentInit();
        equipInit.preInit();
        MinecraftForge.EVENT_BUS.register((Object)INSTANCE);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (ArmorSet e : ArmorSet.values()) {
            e.register();
        }
        for (ToolSet e : ToolSet.values()) {
            e.register();
        }
        equipInit.initialize();
    }

    static {
        ARMOR_MATERIAL_GELID = EnumHelper.addArmorMaterial((String)"GELID", (String)"gelidenderium", (int)100, (int[])new int[]{3, 6, 8, 3}, (int)25, (SoundEvent)SoundEvents.ITEM_ARMOR_EQUIP_IRON, (float)3.0f);
        TOOL_MATERIAL_GELID = EnumHelper.addToolMaterial((String)"GELID", (int)4, (int)100, (float)10.0f, (float)0.0f, (int)25);
    }

    public static class EquipmentInit implements IInitializer, IModelRegister {
    	
        public static ItemFeeder itemFeeder;
        public static ItemCapacitorAmulet itemCapacitorAmulet;
        @GameRegistry.ItemStackHolder(value="thermalexpansion:capacitor", meta=4)
        public static final ItemStack resonantCapacitor;
        @GameRegistry.ItemStackHolder(value="thermalexpansion:capacitor", meta=1)
        public static final ItemStack hardenedCapacitor;
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

        public boolean preInit() {
            this.config();
            itemCapacitorAmulet = new ItemCapacitorAmulet(capacity, transfer);
            itemCapacitorAmulet.setUnlocalizedName("redstonerepository.bauble.capacitor.gelid").setCreativeTab(RedstoneRepository.tabCommon);
            itemCapacitorAmulet.setRegistryName("capacitor_gelid");
            ForgeRegistries.ITEMS.register(itemCapacitorAmulet);
            capacitorAmuletGelid = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)itemCapacitorAmulet), (int)0);
            itemFeeder = new ItemFeeder(hungerPointsMax, feederCapacity, feederMaxTransfer, feederEnergyPerUse, feederMaxSat);
            itemFeeder.setUnlocalizedName("redstonerepository.bauble.feeder").setCreativeTab(RedstoneRepository.tabCommon);
            itemFeeder.setRegistryName("feeder");
            ForgeRegistries.ITEMS.register(itemFeeder);
            feederStack = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)itemFeeder), (int)0);
            RedstoneRepository.proxy.addIModelRegister(this);
            return true;
        }

        public void config() {
            boolean enableConfig = RedstoneRepository.CONFIG.get("Item.Capacitor", "Enable", true, "Enable the Gelid Capacitor Amulet");
            boolean enableLoaded = Loader.isModLoaded((String)"baubles");
            EquipmentInit.enable[0] = enableConfig && enableLoaded;
            transfer = RedstoneRepository.CONFIG.get("Item.Capacitor", "BaseTransfer", 100000, "Set the base transfer rate of the Gelid Capacitor Amulet in RF/t (Default 100,000) ");
            capacity = RedstoneRepository.CONFIG.get("Item.Capacitor", "BaseCapacity", 100000000, "Set the base capacity of the Gelid Capacitor Amulet in RF/t (Default 100,000,000) ");
            boolean enableFeederConfig = RedstoneRepository.CONFIG.get("Item.Feeder", "Enable", true, "Enable the Endoscopic Gastrostomizer (Automatic Feeder)");
            EquipmentInit.enable[1] = enableFeederConfig && enableLoaded;
            hungerPointsMax = RedstoneRepository.CONFIG.get("Item.Feeder", "MaxHungerPoints", 500, "Set the maximum hunger point storage of the Endoscopic Gastrostomizer (EG) (Default 500)");
            feederCapacity = RedstoneRepository.CONFIG.get("Item.Feeder", "BaseCapacity", 4000000, "Set the base capacity of the E.G. in RF (Default 4,000,000) ");
            feederMaxTransfer = RedstoneRepository.CONFIG.get("Item.Feeder", "MaxTransfer", 8000, "Set the maximum transfer rate into the item in RF/t (Default 8000)");
            feederEnergyPerUse = RedstoneRepository.CONFIG.get("Item.Feeder", "EnergyPerUse", 30000, "Set amount of energy used per food point in RF (Default 3000)");
            feederMaxSat = RedstoneRepository.CONFIG.get("Item.Feeder", "SaturationFillLevel", 5, "Maximum amount of hunger saturation to automatically fill to. Higher numbers consume hunger points more quickly. (Default 5, Max 20)");
        }

        public boolean initialize() {
            mushroomStewBucket = FluidUtil.getFilledBucket((FluidStack)FluidRegistry.getFluidStack((String)"mushroom_stew", (int)1000));
            if (enable[0]) {
                RecipeHelper.addShapedRecipe((ItemStack)capacitorAmuletGelid, (Object[])new Object[]{" S ", "ACA", "AGA", Character.valueOf('S'), ItemMaterial.stringFluxed, Character.valueOf('A'), ItemMaterial.plateArmorGelidEnderium, Character.valueOf('G'), ItemMaterial.ingotGelidEnderium, Character.valueOf('C'), resonantCapacitor});
            }
            if (enable[1]) {
                RecipeHelper.addShapedRecipe((ItemStack)feederStack, (Object[])new Object[]{"SCS", "PMP", " G ", Character.valueOf('S'), ItemMaterial.stringFluxed, Character.valueOf('C'), hardenedCapacitor, Character.valueOf('M'), mushroomStewBucket, Character.valueOf('P'), ItemMaterial.plateGelidEnderium, Character.valueOf('G'), ItemMaterial.gearGelidEnderium});
            }
            return true;
        }

        @SideOnly(value=Side.CLIENT)
        public void registerModels() {
            ModelLoader.setCustomModelResourceLocation((Item)itemCapacitorAmulet, (int)0, (ModelResourceLocation)new ModelResourceLocation("redstonerepository:capacitor_gelid", "inventory"));
            ModelLoader.setCustomModelResourceLocation((Item)itemFeeder, (int)0, (ModelResourceLocation)new ModelResourceLocation("redstonerepository:feeder", "inventory"));
        }

        static {
            resonantCapacitor = null;
            hardenedCapacitor = null;
            enable = new boolean[2];
        }
    }

    public static enum ToolSet implements IModelRegister
    {
        GELID("gelid", RedstoneRepositoryEquipment.TOOL_MATERIAL_GELID);
        
        private final String name;
        private final Item.ToolMaterial TOOL_MATERIAL;
        public ItemBattleWrenchGelidEnderium itemBattleWrench;
        public ItemSwordGelidEnderium itemSword;
        public ItemShovelGelidEnderium itemShovel;
        public ItemPickaxeGelidEnderium itemPickaxe;
        public ItemAxeGelidEnderium itemAxe;
        public ItemSickleGelidEnderium itemSickle;
        public ItemStack toolBattleWrench;
        public ItemStack toolSword;
        public ItemStack toolShovel;
        public ItemStack toolPickaxe;
        public ItemStack toolAxe;
        public ItemStack toolSickle;
        public int axeBlocksCutPerTick;
        public boolean[] enable = new boolean[11];

        private ToolSet(String name, Item.ToolMaterial material) {
            this.name = name.toLowerCase(Locale.US);
            this.TOOL_MATERIAL = material;
        }

        protected void create() {
            this.itemBattleWrench = new ItemBattleWrenchGelidEnderium(this.TOOL_MATERIAL);
            this.itemSword = new ItemSwordGelidEnderium(this.TOOL_MATERIAL);
            this.itemShovel = new ItemShovelGelidEnderium(this.TOOL_MATERIAL);
            this.itemPickaxe = new ItemPickaxeGelidEnderium(this.TOOL_MATERIAL);
            this.itemAxe = new ItemAxeGelidEnderium(this.TOOL_MATERIAL, this.axeBlocksCutPerTick);
            this.itemSickle = new ItemSickleGelidEnderium(this.TOOL_MATERIAL);
        }

        protected void config() {
            this.axeBlocksCutPerTick = RedstoneRepository.CONFIG.getConfiguration().get("Equipment.Tools.Axe", "BlocksPerTick", 3, "Sets the number of blocks per tick the axe attempts to cut in empowered mode. Higher values cause more lag. ").setMinValue(0).setMaxValue(10).getInt();
        }

        protected void initialize() {
            String ENDERIUM_LOCALE = "redstonerepository.tool.enderium." + this.name + ".";
            String category = "Equipment.Tools." + StringHelper.titleCase((String)this.name);
            this.enable[1] = RedstoneRepository.CONFIG.getConfiguration().get(category, "BattleWrench", true).getBoolean(true);
            this.enable[2] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Sword", true).getBoolean(true);
            this.enable[3] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Shovel", true).getBoolean(true);
            this.enable[4] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Pickaxe", true).getBoolean(true);
            this.enable[5] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Axe", true).getBoolean(true);
            this.enable[8] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Sickle", true).getBoolean(true);
            this.config();
            this.create();
            this.itemBattleWrench.setUnlocalizedName(ENDERIUM_LOCALE + "battlewrench");
            this.itemBattleWrench.setCreativeTab(RedstoneRepository.tabCommon);
            this.itemBattleWrench.setShowInCreative(this.enable[1]);
            this.itemBattleWrench.setRegistryName("tool.battlewrench_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemBattleWrench);
            this.itemSword.setUnlocalizedName(ENDERIUM_LOCALE + "sword").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemSword.setShowInCreative(this.enable[2]);
            this.itemSword.setRegistryName("tool.sword_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemSword);
            this.itemShovel.setUnlocalizedName(ENDERIUM_LOCALE + "shovel").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemShovel.setShowInCreative(this.enable[3]);
            this.itemShovel.setRegistryName("tool.shovel_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemShovel);
            this.itemPickaxe.setUnlocalizedName(ENDERIUM_LOCALE + "pickaxe").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemPickaxe.setShowInCreative(this.enable[4]);
            this.itemPickaxe.setRegistryName("tool.pickaxe_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemPickaxe);
            this.itemAxe.setUnlocalizedName(ENDERIUM_LOCALE + "axe").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemAxe.setShowInCreative(this.enable[5]);
            this.itemAxe.setRegistryName("tool.axe_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemAxe);
            this.itemSickle.setUnlocalizedName(ENDERIUM_LOCALE + "sickle").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemSickle.setShowInCreative(this.enable[8]);
            this.itemSickle.setRegistryName("tool.sickle_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemSickle);
            this.toolBattleWrench = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemBattleWrench), (int)0);
            this.toolSword = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemSword), (int)0);
            this.toolShovel = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemShovel), (int)0);
            this.toolPickaxe = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemPickaxe), (int)0);
            this.toolAxe = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemAxe), (int)0);
            this.toolSickle = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemSickle), (int)0);
        }

        protected void register() {
            if (this.enable[1]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolBattleWrench, (Object[])new Object[]{"IWI", " G ", " R ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('G'), "gearGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('W'), RAEquipment.ToolSet.FLUX.itemBattleWrench});
            }
            if (this.enable[2]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolSword, (Object[])new Object[]{" I ", " S ", " R ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('S'), RAEquipment.ToolSet.FLUX.itemSword});
            }
            if (this.enable[3]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolShovel, (Object[])new Object[]{" I ", " S ", " R ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('S'), RAEquipment.ToolSet.FLUX.itemShovel});
            }
            if (this.enable[4]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolPickaxe, (Object[])new Object[]{"III", " P ", " R ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('P'), RAEquipment.ToolSet.FLUX.itemPickaxe});
            }
            if (this.enable[5]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolAxe, (Object[])new Object[]{"II ", "IA ", " R ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('A'), RAEquipment.ToolSet.FLUX.itemAxe});
            }
            if (this.enable[8]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.toolSickle, (Object[])new Object[]{" I ", " SI", "RI ", Character.valueOf('I'), "ingotGelidEnderium", Character.valueOf('R'), ItemMaterial.rodGelid, Character.valueOf('S'), RAEquipment.ToolSet.FLUX.itemSickle});
            }
        }

        @SideOnly(value=Side.CLIENT)
        public void registerModel(Item item, String stackName) {
            ModelLoader.setCustomModelResourceLocation((Item)item, (int)0, (ModelResourceLocation)new ModelResourceLocation("redstonerepository:tools/" + stackName, "inventory"));
        }

        @SideOnly(value=Side.CLIENT)
        public void registerModels() {
            this.registerModel((Item)this.itemBattleWrench, "battle_wrench_" + this.name);
            this.registerModel((Item)this.itemSword, "sword_" + this.name);
            this.registerModel((Item)this.itemShovel, "shovel_" + this.name);
            this.registerModel((Item)this.itemPickaxe, "pickaxe_" + this.name);
            this.registerModel((Item)this.itemAxe, "axe_" + this.name);
            this.registerModel((Item)this.itemSickle, "sickle_" + this.name);
        }
    }

    public static enum ArmorSet implements IModelRegister
    {
        GELID("gelid", RedstoneRepositoryEquipment.ARMOR_MATERIAL_GELID);
        
        private final String name;
        private final ItemArmor.ArmorMaterial ARMOR_MATERIAL;
        public ItemArmorEnderium itemHelmet;
        public ItemArmorEnderium itemPlate;
        public ItemArmorEnderium itemLegs;
        public ItemArmorEnderium itemBoots;
        public ItemStack armorHelmet;
        public ItemStack armorPlate;
        public ItemStack armorLegs;
        public ItemStack armorBoots;
        public boolean[] enable = new boolean[4];

        private ArmorSet(String name, ItemArmor.ArmorMaterial material) {
            this.name = name.toLowerCase(Locale.US);
            this.ARMOR_MATERIAL = material;
        }

        protected void create() {
            this.itemHelmet = new ItemArmorEnderium(this.ARMOR_MATERIAL, EntityEquipmentSlot.HEAD);
            this.itemPlate = new ItemArmorEnderium(this.ARMOR_MATERIAL, EntityEquipmentSlot.CHEST);
            this.itemLegs = new ItemArmorEnderium(this.ARMOR_MATERIAL, EntityEquipmentSlot.LEGS);
            this.itemBoots = new ItemArmorEnderium(this.ARMOR_MATERIAL, EntityEquipmentSlot.FEET);
        }

        protected void initialize() {
            String ENDERIUM_LOCALE = "redstonerepository.armor.enderium." + this.name + ".";
            String PATH_ARMOR = "redstonerepository:textures/models/armor/";
            String[] TEXTURE = new String[]{"redstonerepository:textures/models/armor/" + this.name + "_1.png", "redstonerepository:textures/models/armor/" + this.name + "_2.png"};
            String category = "Equipment.Armor." + StringHelper.titleCase((String)this.name);
            this.enable[0] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Helmet", true).getBoolean(true);
            this.enable[1] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Chestplate", true).getBoolean(true);
            this.enable[2] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Leggings", true).getBoolean(true);
            this.enable[3] = RedstoneRepository.CONFIG.getConfiguration().get(category, "Boots", true).getBoolean(true);
            this.create();
            this.itemHelmet.setArmorTextures(TEXTURE).setUnlocalizedName(ENDERIUM_LOCALE + "helm").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemHelmet.setShowInCreative(this.enable[0]);
            this.itemHelmet.setRegistryName("armor.helmet_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemHelmet);
            this.itemPlate.setArmorTextures(TEXTURE).setUnlocalizedName(ENDERIUM_LOCALE + "chestplate").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemPlate.setShowInCreative(this.enable[1]);
            this.itemPlate.setRegistryName("armor.plate_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemPlate);
            this.itemLegs.setArmorTextures(TEXTURE).setUnlocalizedName(ENDERIUM_LOCALE + "leggings").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemLegs.setShowInCreative(this.enable[2]);
            this.itemLegs.setRegistryName("armor.legs_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemLegs);
            this.itemBoots.setArmorTextures(TEXTURE).setUnlocalizedName(ENDERIUM_LOCALE + "boots").setCreativeTab(RedstoneRepository.tabCommon);
            this.itemBoots.setShowInCreative(this.enable[3]);
            this.itemBoots.setRegistryName("armor.boots_" + this.name);
            ForgeRegistries.ITEMS.register(this.itemBoots);
            this.armorHelmet = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemHelmet), (int)0);
            this.armorPlate = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemPlate), (int)0);
            this.armorLegs = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemLegs), (int)0);
            this.armorBoots = EnergyHelper.setDefaultEnergyTag((ItemStack)new ItemStack((Item)this.itemBoots), (int)0);
        }

        protected void register() {
            if (this.enable[0]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.armorHelmet, (Object[])new Object[]{"III", "IAI", Character.valueOf('I'), ItemMaterial.plateArmorGelidEnderium, Character.valueOf('A'), RAEquipment.ArmorSet.FLUX.itemHelmet});
            }
            if (this.enable[1]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.armorPlate, (Object[])new Object[]{"IAI", "III", "III", Character.valueOf('I'), ItemMaterial.plateArmorGelidEnderium, Character.valueOf('A'), RAEquipment.ArmorSet.FLUX.itemPlate});
            }
            if (this.enable[2]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.armorLegs, (Object[])new Object[]{"III", "IAI", "I I", Character.valueOf('I'), ItemMaterial.plateArmorGelidEnderium, Character.valueOf('A'), RAEquipment.ArmorSet.FLUX.itemLegs});
            }
            if (this.enable[3]) {
                RecipeHelper.addShapedRecipe((ItemStack)this.armorBoots, (Object[])new Object[]{"IAI", "I I", Character.valueOf('I'), ItemMaterial.plateArmorGelidEnderium, Character.valueOf('A'), RAEquipment.ArmorSet.FLUX.itemBoots});
            }
        }

        @SideOnly(value=Side.CLIENT)
        public void registerModel(Item item, String stackName) {
            ModelLoader.setCustomModelResourceLocation((Item)item, (int)0, (ModelResourceLocation)new ModelResourceLocation("redstonerepository:armor", "type=" + stackName));
        }

        @SideOnly(value=Side.CLIENT)
        public void registerModels() {
            this.registerModel((Item)this.itemHelmet, "helmet_" + this.name);
            this.registerModel((Item)this.itemPlate, "chestplate_" + this.name);
            this.registerModel((Item)this.itemLegs, "leggings_" + this.name);
            this.registerModel((Item)this.itemBoots, "boots_" + this.name);
        }
    }

}

