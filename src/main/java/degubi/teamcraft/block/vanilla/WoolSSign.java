package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class WoolSSign extends BlockSign{
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

    public WoolSSign(){
        setDefaultState(blockState.getBaseState().withProperty(WoolSSign.ROTATION, Integer.valueOf(0)));
        setCreativeTab(Main.tabDecorations);
        setHardness(Blocks.PLANKS.getDefaultState().getBlockHardness(null, null));
        setResistance(Blocks.PLANKS.getExplosionResistance(null, null, null, null));
        setSoundType(Blocks.PLANKS.getSoundType(null, null, null, null));
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
        if (!world.getBlockState(pos.down()).getMaterial().isSolid()){
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(WoolSSign.ROTATION, Integer.valueOf(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(WoolSSign.ROTATION).intValue();
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, WoolSSign.ROTATION);
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
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Item.getItemFromBlock(Main.WoolStandingSign));
    }
}