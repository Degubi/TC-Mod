package degubi.teamcraft.block;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.entity.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockMobIceBlocks extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    public BlockMobIceBlocks() {
        super(Material.ICE);
        
        Block modelBlock = Blocks.ICE;
        setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
        blockParticleGravity = 0;
        setDefaultSlipperiness(0.98F);
        setCreativeTab(Main.tabAdmin);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        spawnMobOnBreak(world, pos, state);
        world.setBlockToAir(pos.up());
        super.breakBlock(world, pos, state);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return EnumFacing.getFront(meta).getAxis() == EnumFacing.Axis.Y ? getDefaultState().withProperty(FACING, EnumFacing.NORTH) : getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(FACING).getIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess acc, BlockPos pos, EnumFacing side){
        return !acc.getBlockState(pos.offset(side)).isOpaqueCube() && (side != EnumFacing.UP || this == Main.IcePigSpiderBlock);
    }
    
    private static void spawnMobOnBreak(World world, BlockPos pos, IBlockState state){
        if(!world.isRemote){
            Block block = state.getBlock();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            
            if(block == Main.IceCreeperBlock){
                EntityCreeper creeper = new EntityCreeper(world);
                creeper.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                world.spawnEntity(creeper);
            }else if(block == Main.IcePigmanBlock){
                EntityPigman pigman = new EntityPigman(world);
                pigman.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                pigman.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Main.LasulitSword));
                world.spawnEntity(pigman);
            }else if(block == Main.IcePigSpiderBlock){
                EntityPigSpider spider = new EntityPigSpider(world);
                spider.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                world.spawnEntity(spider);
            }else if(block == Main.IceSkeletonBlock){
                EntitySkeleton skeleton = new EntitySkeleton(world);
                skeleton.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Main.EmeraldSword));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Main.LapisHelmet));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Main.LapisChestPlate));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Main.LapisLeggings));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Main.LapisBoots));
                world.spawnEntity(skeleton);
            }else if(block == Main.IceZombieBlock){
                EntityZombie zombie = new EntityZombie(world);
                zombie.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                zombie.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                world.spawnEntity(zombie);
            }else if(block == Main.IceThreeHeadCreeperBlock){
                EntityThreeHeadCreeper threeh = new EntityThreeHeadCreeper(world);
                threeh.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                world.spawnEntity(threeh);
            }else{
                EntityGoat goat = new EntityGoat(world);
                goat.setLocationAndAngles(x + 0.5F, y, z + 0.5F, 0, 0);
                goat.setGoatType(2);
                world.spawnEntity(goat);
            }
        }
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state){
        return new TileEntityIceBlock();
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Block.getBlockById(0));
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemById(0);
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }
}