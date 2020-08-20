package degubi.teamcraft.block;

import degubi.teamcraft.*;
import degubi.teamcraft.worldgen.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

public final class BlockAppleSapling extends BlockBush implements IGrowable{
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 4);

    public BlockAppleSapling(){
        super(Material.LEAVES);
        setSoundType(SoundType.PLANT);
        setCreativeTab(Main.tabDecorations);
    }
    
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient){
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state){
        return rand.nextFloat() < 0.45D;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BUSH_AABB;
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        if(!world.isRemote && world.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0){
            this.grow(world, rand, pos, state);
        }
        super.updateTick(world, pos, state, rand);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(STAGE, Integer.valueOf(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(STAGE).intValue();
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, STAGE);
    }
    
    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state){
        if(state.getValue(STAGE).intValue() < 4) {
            world.setBlockState(pos, state.cycleProperty(STAGE));
        }else{
            if(state.getBlock() == Main.AppleSapling) {
                new WorldGenAppleTree().generate(world, rand, pos);
            }else {
                new WorldGenTrees(true, 10, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), Main.bananaLeaves.getDefaultState(), false).generate(world, rand, pos);
            }
        }
    }
}