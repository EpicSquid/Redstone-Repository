package thundr.redstonerepository.block;

import cofh.core.block.BlockCore;
import cofh.core.block.ItemBlockCore;
import cofh.core.render.IModelRegister;
import cofh.core.util.core.IInitializer;
import cofh.core.util.helpers.DamageHelper;
import cofh.core.util.helpers.EnergyHelper;
import cofh.core.util.helpers.ItemHelper;
import cofh.core.util.helpers.RecipeHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thundr.redstonerepository.RedstoneRepository;

import java.util.Locale;

public class BlockStorage extends BlockCore implements IInitializer, IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);
    static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.00390625D, 0.00390625D, 0.00390625D, 0.99609375D, 0.99609375D, 0.99609375D);
    public static ItemStack blockGelidEnderium;
    public static ItemStack blockGelidCrystal;

    public BlockStorage() {
        super(Material.IRON, RedstoneRepository.MODID);
        this.setUnlocalizedName("storage");
        this.setCreativeTab(RedstoneRepository.tabCommon);
        this.setHardness(25.0f);
        this.setResistance(120.0f);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.GELID_ENDERIUM));
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < Type.METADATA_LOOKUP.length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.redstonerepository.storage." + Type.values()[ItemHelper.getItemDamage(stack)].getNameRaw() + ".name";
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return Type.values()[ItemHelper.getItemDamage(stack)].getRarity();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Type.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.attackEntityFrom(DamageHelper.FLUX, 2.0F);
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (EnergyHelper.isPlayerHoldingEnergyContainerItem(player)) {
                EnergyHelper.insertEnergyIntoHeldContainer(player, 100, false);
            }
        }
    }

    @Override
    public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(VARIANT).getLight();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return state.getValue(VARIANT).getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(VARIANT).getResistance();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return COLLISION_AABB;
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < Type.values().length; ++i) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(RedstoneRepository.MODID + ":" + this.name, "type=" + Type.values()[i].getName()));
        }
    }

    @Override
    public boolean preInit() {
        this.setRegistryName("storage");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(this.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
        blockGelidEnderium = new ItemStack(this, 1, Type.GELID_ENDERIUM.getMetadata());
        blockGelidCrystal = new ItemStack(this, 1, Type.GELID_CRYSTAL.getMetadata());
        ItemHelper.registerWithHandlers("blockGelidEnderium", blockGelidEnderium);
        ItemHelper.registerWithHandlers("blockGelidCrystal", blockGelidCrystal);
        RedstoneRepository.PROXY.addIModelRegister(this);
        return true;
    }

    @Override
    public boolean initialize() {
        RecipeHelper.addStorageRecipe(blockGelidEnderium, "ingotGelidEnderium");
        RecipeHelper.addStorageRecipe(blockGelidCrystal, "gemGelidCrystal");
        return true;
    }

    public enum Type implements IStringSerializable {
        GELID_ENDERIUM(0, "gelidEnderium", blockGelidEnderium, 7),
        GELID_CRYSTAL(1, "gelidCrystal", blockGelidCrystal, 7);

        private static final Type[] METADATA_LOOKUP = new Type[values().length];
        private final int metadata;
        private final String name;
        private final ItemStack stack;
        private final int light;
        private final float hardness;
        private final float resistance;
        private final EnumRarity rarity;

        Type(int metadata, String name, ItemStack stack, int light, float hardness, float resistance, EnumRarity rarity) {
            this.metadata = metadata;
            this.name = name;
            this.stack = stack;
            this.light = light;
            this.hardness = hardness;
            this.resistance = resistance;
            this.rarity = rarity;
        }

        Type(int metadata, String name, ItemStack stack, int light, float hardness, float resistance) {

            this(metadata, name, stack, light, hardness, resistance, EnumRarity.UNCOMMON);
        }

        Type(int metadata, String name, ItemStack stack, float hardness, float resistance) {

            this(metadata, name, stack, 0, hardness, resistance, EnumRarity.UNCOMMON);
        }

        Type(int metadata, String name, ItemStack stack, int light) {

            this(metadata, name, stack, light, 25.0F, 120.0F, EnumRarity.UNCOMMON);
        }

        Type(int metadata, String name, ItemStack stack) {

            this(metadata, name, stack, 0, 25.0F, 120.0F, EnumRarity.UNCOMMON);
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
    }
}

