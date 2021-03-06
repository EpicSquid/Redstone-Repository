package thundr.redstonerepository.blocks;

import cofh.core.block.BlockCore;
import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thundr.redstonerepository.RedstoneRepository;
import thundr.redstonerepository.items.blocks.ItemBlockStorage;
import thundr.redstonerepository.proxies.CommonProxy;

public class BlockStorage extends BlockCore implements IInitializer, IModelRegister {
	
    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create((String)"type", Type.class);
    public static ItemStack blockGelidEnderium;
    public static ItemStack blockGelidGem;

    public BlockStorage() {
        super(Material.IRON, "redstonerepository");
        this.setUnlocalizedName("storage");
        this.setCreativeTab(RedstoneRepository.tabCommon);
        this.setHardness(25.0f);
        this.setResistance(120.0f);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Type.GELID_ENDERIUM)));
        this.setHarvestLevel("pickaxe", 2);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block)this, new IProperty[]{VARIANT});
    }

    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < METADATA_LOOKUP.length; ++i) {
            items.add((ItemStack)new ItemStack((Block)this, 1, i));
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, (Type.byMetadata(meta)));
    }

    public int getMetaFromState(IBlockState state) {
        return ((Type)((Object)state.getValue(VARIANT))).getMetadata();
    }

    public int damageDropped(IBlockState state) {
        return ((Type)((Object)state.getValue(VARIANT))).getMetadata();
    }

    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ((Type)((Object)state.getValue(VARIANT))).getLight();
    }

    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return ((Type)((Object)state.getValue(VARIANT))).getHardness();
    }

    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        return ((Type)((Object)state.getValue(VARIANT))).getResistance();
    }

    @SideOnly(value=Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < Type.values().length; ++i) {
            ModelLoader.setCustomModelResourceLocation((Item)Item.getItemFromBlock((Block)this), (int)i, (ModelResourceLocation)new ModelResourceLocation("redstonerepository:" + this.name, "type=" + Type.byMetadata(i).getName()));
        }
    }

    public boolean preInit() {
        this.setRegistryName("storage");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockStorage itemBlock = new ItemBlockStorage((Block)this);
        itemBlock.setRegistryName(this.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
        blockGelidEnderium = new ItemStack((Block)this, 1, Type.GELID_ENDERIUM.getMetadata());
        blockGelidGem = new ItemStack((Block)this, 1, Type.GELID_GEM.getMetadata());
        ItemHelper.registerWithHandlers((String)"blockGelidEnderium", (ItemStack)blockGelidEnderium);
        ItemHelper.registerWithHandlers((String)"blockGelidGem", (ItemStack)blockGelidGem);
        RedstoneRepository.proxy.addIModelRegister(this);
        return true;
    }

    public boolean initialize() {
        RecipeHelper.addStorageRecipe((ItemStack)blockGelidEnderium, (String)"ingotGelidEnderium");
        RecipeHelper.addStorageRecipe((ItemStack)blockGelidGem, (String)"gemGelid");
        return true;
    }

    public static enum Type implements IStringSerializable
    {
        GELID_ENDERIUM(0, "blockGelidEnderium", BlockStorage.blockGelidEnderium, 7),
        GELID_GEM(1, "blockGelidGem", BlockStorage.blockGelidGem);
        
        private static final Type[] METADATA_LOOKUP;
        private final int metadata;
        private final String name;
        private final ItemStack stack;
        private final int light;
        private final float hardness;
        private final float resistance;
        private final EnumRarity rarity;

        private Type(int metadata, String name, ItemStack stack, int light, float hardness, float resistance, EnumRarity rarity) {
            this.metadata = metadata;
            this.name = name;
            this.stack = stack;
            this.light = light;
            this.hardness = hardness;
            this.resistance = resistance;
            this.rarity = rarity;
        }

        private Type(int metadata, String name, ItemStack stack, int light, float hardness, float resistance) {
            this(metadata, name, stack, light, hardness, resistance, EnumRarity.RARE);
        }

        private Type(int metadata, String name, ItemStack stack, float hardness, float resistance) {
            this(metadata, name, stack, 0, hardness, resistance, EnumRarity.RARE);
        }

        private Type(int metadata, String name, ItemStack stack, int light) {
            this(metadata, name, stack, light, 25.0f, 120.0f, EnumRarity.RARE);
        }

        private Type(int metadata, String name, ItemStack stack) {
            this(metadata, name, stack, 0, 25.0f, 120.0f, EnumRarity.RARE);
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getName() {
            return this.name.toLowerCase(Locale.US);
        }

        public String getNameRaw() {
            return this.name;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public int getLight() {
            return this.light;
        }

        public float getHardness() {
            return this.hardness;
        }

        public float getResistance() {
            return this.resistance;
        }

        public EnumRarity getRarity() {
            return this.rarity;
        }

        public static Type byMetadata(int metadata) {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
                metadata = 0;
            }
            return METADATA_LOOKUP[metadata];
        }

        static {
            METADATA_LOOKUP = new Type[Type.values().length];
            Type[] arrtype = Type.values();
            int n = arrtype.length;
            for (int i = 0; i < n; ++i) {
                Type type;
                Type.METADATA_LOOKUP[type.getMetadata()] = type = arrtype[i];
            }
        }
    }

}

