package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockConveyor extends Block {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 0.95D, 1);

    public BlockConveyor() {
        super(Material.ROCK);

        Block modelBlock = Blocks.STONE;
        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.EAST));
        setCreativeTab(Main.tabDecorations);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return getDefaultState().withProperty(BlockHorizontal.FACING, (facing != EnumFacing.UP && facing != EnumFacing.DOWN ? facing : placer.getHorizontalFacing()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }
        return getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(BlockHorizontal.FACING).getIndex();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(!heldItem.isEmpty() && heldItem.getItem() == Main.tcmMultiTool){
            world.setBlockState(pos, state.cycleProperty(BlockHorizontal.FACING));
            return true;
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BOX;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void onLanded(World world, Entity entity){}

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
        EnumFacing facing = state.getValue(BlockHorizontal.FACING);

        if(facing == EnumFacing.NORTH){
            entity.motionZ += -0.1F;
        }else if(facing == EnumFacing.SOUTH){
            entity.motionZ += 0.1F;
        }else if(facing == EnumFacing.EAST){
            entity.motionX += 0.1F;
        }else{
            entity.motionX += -0.1F;
        }
    }
}