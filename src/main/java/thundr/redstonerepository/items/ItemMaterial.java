package thundr.redstonerepository.items;

import cofh.api.util.ThermalExpansionHelper;
import cofh.core.item.ItemMulti;
import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.proxies.CommonProxy;

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
        RedstoneRepository.proxy.addIModelRegister((IModelRegister)this);
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
        RecipeHelper.addTwoWayStorageRecipe((ItemStack)ingotGelidEnderium, (String)"ingotGelidEnderium", (ItemStack)nuggetGelidEnderium, (String)"nuggetGelidEnderium");
        RecipeHelper.addReverseStorageRecipe((ItemStack)ingotGelidEnderium, (String)"blockGelidEnderium");
        RecipeHelper.addReverseStorageRecipe((ItemStack)gemGelid, (String)"blockGelidGem");
        RecipeHelper.addGearRecipe((ItemStack)gearGelidEnderium, (String)"ingotGelidEnderium");
        RecipeHelper.addShapedRecipe((ItemStack)rodGelid, (Object[])new Object[]{"  O", " B ", "O  ", Character.valueOf('B'), cofh.redstonearsenal.item.ItemMaterial.rodObsidian, Character.valueOf('O'), "gemGelid"});
        RecipeHelper.addShapedRecipe((ItemStack)plateArmorGelidEnderium, (Object[])new Object[]{" I ", "IGI", " I ", Character.valueOf('G'), "gemGelid", Character.valueOf('I'), "plateGelidEnderium"});
        ItemStack dustEnderium = ItemHelper.cloneStack((ItemStack)((ItemStack)OreDictionary.getOres((String)"dustEnderium", (boolean)false).get(0)), (int)1);
        FluidStack fluidCryotheum = new FluidStack(FluidRegistry.getFluid((String)"cryotheum"), 1000);
        FluidStack fluidRedstone = new FluidStack(FluidRegistry.getFluid((String)"redstone"), 200);
        ThermalExpansionHelper.addSmelterRecipe((int)4000, (ItemStack)dustGelidEnderium, (ItemStack)new ItemStack((Block)Blocks.SAND), (ItemStack)ingotGelidEnderium);
        ThermalExpansionHelper.addTransposerFill((int)4000, (ItemStack)dustEnderium, (ItemStack)dustGelidEnderium, (FluidStack)fluidCryotheum, (boolean)false);
        ThermalExpansionHelper.addTransposerFill((int)4000, (ItemStack)new ItemStack(Items.EMERALD), (ItemStack)gemGelid, (FluidStack)fluidCryotheum, (boolean)false);
        ThermalExpansionHelper.addTransposerFill((int)2000, (ItemStack)new ItemStack(Items.STRING), (ItemStack)stringFluxed, (FluidStack)fluidRedstone, (boolean)false);
        return true;
    }
}

