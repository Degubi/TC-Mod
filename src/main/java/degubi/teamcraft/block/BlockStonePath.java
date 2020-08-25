package degubi.teamcraft.block;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockStonePath extends Block {
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.0125F, 1);

    public BlockStonePath() {
        super(Material.ROCK);

        Block modelBlock = Blocks.STONE;
        setCreativeTab(Main.tabDecorations);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos){
        if(!world.getBlockState(pos.down()).getMaterial().isSolid()){
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isOpaqueCube() && world.isAirBlock(pos);
    }
}