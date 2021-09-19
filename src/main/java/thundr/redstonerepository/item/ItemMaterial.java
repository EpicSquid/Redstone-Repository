package thundr.redstonerepository.item;

import cofh.api.util.ThermalExpansionHelper;
import cofh.core.item.ItemMulti;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import thundr.redstonerepository.RedstoneRepository;

public class ItemMaterial extends ItemMulti implements IInitializer {

    public static ItemStack dustGelidEnderium;
    public static ItemStack ingotGelidEnderium;
    public static ItemStack nuggetGelidEnderium;
    public static ItemStack gearGelidEnderium;
    public static ItemStack plateGelidEnderium;
    public static ItemStack gemGelidCrystal;
    public static ItemStack rodGelidObsidian;
    public static ItemStack plateArmorGelidEnderium;
    public static ItemStack stringFluxed;
    public static ItemStack coinGelidEnderium;

    public ItemMaterial() {
        super(RedstoneRepository.MODID);
        this.setUnlocalizedName("material");
        this.setCreativeTab(RedstoneRepository.tabCommon);
    }

    public boolean preInit() {
        ForgeRegistries.ITEMS.register(this.setRegistryName("material"));
        RedstoneRepository.PROXY.addIModelRegister(this);

        dustGelidEnderium = this.addOreDictItem(0, "dustGelidEnderium", EnumRarity.RARE);
        ingotGelidEnderium = this.addOreDictItem(1, "ingotGelidEnderium", EnumRarity.RARE);
        nuggetGelidEnderium = this.addOreDictItem(2, "nuggetGelidEnderium", EnumRarity.RARE);
        gearGelidEnderium = this.addOreDictItem(3, "gearGelidEnderium", EnumRarity.RARE);
        plateGelidEnderium = this.addOreDictItem(4, "plateGelidEnderium", EnumRarity.RARE);
        gemGelidCrystal = this.addOreDictItem(5, "gemGelidCrystal", EnumRarity.RARE);
        rodGelidObsidian = this.addItem(6, "rodGelidObsidian", EnumRarity.RARE);
        plateArmorGelidEnderium = this.addItem(7, "plateArmorGelidEnderium", EnumRarity.RARE);
        stringFluxed = this.addOreDictItem(8, "stringFluxed", EnumRarity.UNCOMMON);
        coinGelidEnderium = this.addOreDictItem(9, "coinGelidEnderium", EnumRarity.RARE);

        return true;
    }

    public boolean initialize() {
        RecipeHelper.addTwoWayStorageRecipe(ingotGelidEnderium, "ingotGelidEnderium", nuggetGelidEnderium, "nuggetGelidEnderium");
        RecipeHelper.addReverseStorageRecipe(ingotGelidEnderium, "blockGelidEnderium");
        RecipeHelper.addReverseStorageRecipe(gemGelidCrystal, "blockGelidCrystal");
        RecipeHelper.addGearRecipe(gearGelidEnderium, "ingotGelidEnderium");
        RecipeHelper.addShapedRecipe(rodGelidObsidian, "  O", " B ", "O  ", 'B', cofh.redstonearsenal.item.ItemMaterial.rodObsidian, 'O', "gemGelidCrystal");
        RecipeHelper.addShapedRecipe(plateArmorGelidEnderium, " I ", "IGI", " I ", 'G', "gemGelidCrystal", 'I', "plateGelidEnderium");

        ItemStack dustEnderium = ItemHelper.cloneStack(OreDictionary.getOres("dustEnderium", false).get(0), 1);
        FluidStack fluidCryotheum = new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000);
        FluidStack fluidRedstone = new FluidStack(FluidRegistry.getFluid("redstone"), 200);

        ThermalExpansionHelper.addSmelterRecipe(4_000, dustGelidEnderium, new ItemStack(Blocks.SAND), ingotGelidEnderium);
        ThermalExpansionHelper.addTransposerFill(4_000, dustEnderium, dustGelidEnderium, fluidCryotheum, false);
        ThermalExpansionHelper.addTransposerFill(4_000, new ItemStack(Items.EMERALD), gemGelidCrystal, fluidCryotheum, false);
        ThermalExpansionHelper.addTransposerFill(2_000, new ItemStack(Items.STRING), stringFluxed, fluidRedstone, false);
        ThermalExpansionHelper.addCompactorCoinRecipe(5_000, ingotGelidEnderium, ItemHelper.cloneStack(coinGelidEnderium, 3));
        ThermalExpansionHelper.addNumismaticFuel(coinGelidEnderium, 200_000);

        return true;
    }
}

