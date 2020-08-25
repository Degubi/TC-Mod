package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import java.util.*;
import javax.annotation.*;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockWalls extends Block {
    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] {AABB_BY_INDEX[0].setMaxY(1.5D), AABB_BY_INDEX[1].setMaxY(1.5D), AABB_BY_INDEX[2].setMaxY(1.5D), AABB_BY_INDEX[3].setMaxY(1.5D), AABB_BY_INDEX[4].setMaxY(1.5D), AABB_BY_INDEX[5].setMaxY(1.5D), AABB_BY_INDEX[6].setMaxY(1.5D), AABB_BY_INDEX[7].setMaxY(1.5D), AABB_BY_INDEX[8].setMaxY(1.5D), AABB_BY_INDEX[9].setMaxY(1.5D), AABB_BY_INDEX[10].setMaxY(1.5D), AABB_BY_INDEX[11].setMaxY(1.5D), AABB_BY_INDEX[12].setMaxY(1.5D), AABB_BY_INDEX[13].setMaxY(1.5D), AABB_BY_INDEX[14].setMaxY(1.5D), AABB_BY_INDEX[15].setMaxY(1.5D)};

    private final Block modelBlock;

    public BlockWalls(Block modelBlock){
        super(modelBlock.getDefaultState().getMaterial());
        this.modelBlock = modelBlock;

        setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.FALSE).withProperty(NORTH, Boolean.FALSE).withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE).withProperty(WEST, Boolean.FALSE));
        setCreativeTab(Main.tabBlocks);
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
        return AABB_BY_INDEX[getAABBIndex(this.getActualState(state, source, pos))];
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        if(modelBlock == Blocks.ICE && world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - state.getLightOpacity(world, pos)){
            if(!world.provider.isSurfaceWorld()){
                world.setBlockToAir(pos);
            }

            dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockState(pos, Blocks.WATER.getDefaultState());
        }
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return 0;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return modelBlock == Blocks.ICE || modelBlock == Blocks.LEAVES ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState){
        IBlockState mehState = state;
        if (!isActualState){
            mehState = this.getActualState(mehState, world, pos);
        }
        addCollisionBoxToList(pos, entityBox, collidingBoxes, CLIP_AABB_BY_INDEX[getAABBIndex(mehState)]);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
        IBlockState mehState = this.getActualState(state, world, pos);
        return CLIP_AABB_BY_INDEX[getAABBIndex(mehState)];
    }

    private static int getAABBIndex(IBlockState state){
        int i = 0;
        if (state.getValue(NORTH).booleanValue()){
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (state.getValue(EAST).booleanValue()){
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (state.getValue(SOUTH).booleanValue()){
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (state.getValue(WEST).booleanValue()){
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }
        return i;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos){
        return false;
    }

    private static boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(world, pos, facing);
        boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || blockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;
        return !isExcepBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID || flag;
    }

    private static boolean isExcepBlockForAttachWithPiston(Block block){
        return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
        if(side != EnumFacing.DOWN && world.getBlockState(pos.offset(side)) == world.getBlockState(pos)) {
            return false;
        }

        if(side == EnumFacing.DOWN) {
            return super.shouldSideBeRendered(state, world, pos, side);
        }

       return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos){
        boolean flag = canWallConnectTo(world, pos, EnumFacing.NORTH);
        boolean flag1 = canWallConnectTo(world, pos, EnumFacing.EAST);
        boolean flag2 = canWallConnectTo(world, pos, EnumFacing.SOUTH);
        boolean flag3 = canWallConnectTo(world, pos, EnumFacing.WEST);
        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;
        return state.withProperty(UP, Boolean.valueOf(!flag4 || !world.isAirBlock(pos.up()))).withProperty(NORTH, Boolean.valueOf(flag)).withProperty(EAST, Boolean.valueOf(flag1)).withProperty(SOUTH, Boolean.valueOf(flag2)).withProperty(WEST, Boolean.valueOf(flag3));
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, UP, NORTH, EAST, WEST, SOUTH);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face){
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        Block connector = world.getBlockState(pos.offset(facing)).getBlock();
        return connector instanceof BlockWalls || connector instanceof BlockFenceGate;
    }

    private static boolean canWallConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        BlockPos other = pos.offset(facing);
        return world.getBlockState(other).getBlock().canBeConnectedTo(world, other, facing.getOpposite()) || canConnectTo(world, other, facing.getOpposite());
    }
}