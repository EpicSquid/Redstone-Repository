package thundr.redstonerepository.items;

import cofh.api.util.ThermalExpansionHelper;
import cofh.core.item.ItemMulti;
import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
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
    public static ItemStack gemGelid;
    public static ItemStack rodGelid;
    public static ItemStack plateArmorGelidEnderium;
    public static ItemStack stringFluxed;

    public ItemMaterial() {
        super("redstonerepository");
        this.setUnlocalizedName("material");
        this.setCreativeTab(RedstoneRepository.tabCommon);
    }

    public boolean preInit() {
        ForgeRegistries.ITEMS.register(this.setRegistryName("material"));
        RedstoneRepository.proxy.addIModelRegister(this);
        dustGelidEnderium = this.addOreDictItem(0, "dustGelidEnderium", EnumRarity.RARE);
        ingotGelidEnderium = this.addOreDictItem(1, "ingotGelidEnderium", EnumRarity.RARE);
        nuggetGelidEnderium = this.addOreDictItem(2, "nuggetGelidEnderium", EnumRarity.RARE);
        gearGelidEnderium = this.addOreDictItem(3, "gearGelidEnderium", EnumRarity.RARE);
        plateGelidEnderium = this.addOreDictItem(4, "plateGelidEnderium", EnumRarity.RARE);
        gemGelid = this.addOreDictItem(5, "gemGelid", EnumRarity.RARE);
        rodGelid = this.addItem(6, "rodGelid", EnumRarity.RARE);
        plateArmorGelidEnderium = this.addItem(7, "plateArmorGelidEnderium", EnumRarity.RARE);
        stringFluxed = this.addOreDictItem(8, "stringFluxed", EnumRarity.UNCOMMON);
        return true;
    }

    public boolean initialize() {
        RecipeHelper.addTwoWayStorageRecipe(ingotGelidEnderium, "ingotGelidEnderium", nuggetGelidEnderium, "nuggetGelidEnderium");
        RecipeHelper.addReverseStorageRecipe(ingotGelidEnderium, "blockGelidEnderium");
        RecipeHelper.addReverseStorageRecipe(gemGelid, "blockGelidGem");
        RecipeHelper.addGearRecipe(gearGelidEnderium, "ingotGelidEnderium");
        RecipeHelper.addShapedRecipe(rodGelid, new Object[]{"  O", " B ", "O  ", Character.valueOf('B'), cofh.redstonearsenal.item.ItemMaterial.rodObsidian, Character.valueOf('O'), "gemGelid"});
        RecipeHelper.addShapedRecipe(plateArmorGelidEnderium, new Object[]{" I ", "IGI", " I ", Character.valueOf('G'), "gemGelid", Character.valueOf('I'), "plateGelidEnderium"});
        ItemStack dustEnderium = ItemHelper.cloneStack(OreDictionary.getOres("dustEnderium", false).get(0), 1);
        FluidStack fluidCryotheum = new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000);
        FluidStack fluidRedstone = new FluidStack(FluidRegistry.getFluid("redstone"), 200);
        ThermalExpansionHelper.addSmelterRecipe(4000, dustGelidEnderium, new ItemStack(Blocks.SAND), ingotGelidEnderium);
        ThermalExpansionHelper.addTransposerFill(4000, dustEnderium, dustGelidEnderium, fluidCryotheum, false);
        ThermalExpansionHelper.addTransposerFill(4000, new ItemStack(Items.EMERALD), gemGelid, fluidCryotheum, false);
        ThermalExpansionHelper.addTransposerFill(2000, new ItemStack(Items.STRING), stringFluxed, fluidRedstone, false);
        return true;
    }
}

