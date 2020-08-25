package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class WoolWSign extends BlockSign{
    private static final AxisAlignedBB[] boxes = {new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 0.125D, 0.78125D, 1.0D), new AxisAlignedBB(0.875D, 0.28125D, 0.0D, 1.0D, 0.78125D, 1.0D), new AxisAlignedBB(0.0D, 0.28125D, 0.0D, 1.0D, 0.78125D, 0.125D), new AxisAlignedBB(0.0D, 0.28125D, 0.875D, 1.0D, 0.78125D, 1.0D)};

    public WoolWSign(){
        setDefaultState(getBlockState().getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        setHardness(Blocks.PLANKS.getDefaultState().getBlockHardness(null, null));
        setResistance(Blocks.PLANKS.getExplosionResistance(null, null, null, null));
        setSoundType(Blocks.PLANKS.getSoundType(null, null, null, null));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        switch(state.getValue(BlockHorizontal.FACING)){
            case SOUTH:
                return boxes[2];
            case WEST:
                return boxes[1];
            case EAST:
                return boxes[0];
            default:
                return boxes[3];
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
        if(!world.getBlockState(pos.offset(state.getValue(BlockHorizontal.FACING).getOpposite())).getMaterial().isSolid()){
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(BlockHorizontal.FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new TileEntityWoolSign();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Main.WoolStandingSign);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Item.getItemFromBlock(Main.WoolStandingSign));
    }
}