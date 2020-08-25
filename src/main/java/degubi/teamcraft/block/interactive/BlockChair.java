package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import degubi.teamcraft.entity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockChair extends Block {
    public static final PropertyBool isOccupied = PropertyBool.create("isoccupied");
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.19F, 0F, 0.19F, 0.81F, 1F, 0.81F);

    public BlockChair() {
        super(Material.WOOD);

        Block modelBlock = Blocks.PLANKS;
        setDefaultState(getBlockState().getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH).withProperty(isOccupied, Boolean.FALSE));
        setCreativeTab(Main.tabDecorations);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BOUNDING_BOX;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        int mehta = meta;
        if(mehta > 5) {
            mehta /= 2;
        }

        EnumFacing enumfacing = EnumFacing.getFront(mehta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }
        return mehta > 5 ? getDefaultState().withProperty(isOccupied, Boolean.TRUE).withProperty(BlockHorizontal.FACING, enumfacing) : getDefaultState().withProperty(isOccupied, Boolean.FALSE).withProperty(BlockHorizontal.FACING, enumfacing);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(!world.isRemote){
            for(EntityChair chair : world.getEntitiesWithinAABB(EntityChair.class, new AxisAlignedBB(pos))){
                chair.setDead();
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing());
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(isOccupied).booleanValue() ? state.getValue(BlockHorizontal.FACING).getIndex() : state.getValue(BlockHorizontal.FACING).getIndex() + 5;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockHorizontal.FACING, isOccupied);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !state.getValue(isOccupied).booleanValue()){
            world.setBlockState(pos, state.withProperty(isOccupied, Boolean.TRUE));
            EntityChair chair = new EntityChair(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            world.spawnEntity(chair);
            player.startRiding(chair);
        }
        return true;
    }
}