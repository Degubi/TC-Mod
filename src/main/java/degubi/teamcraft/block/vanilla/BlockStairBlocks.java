package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockStairBlocks extends BlockStairs {
    private final Block modelBlock;
    
    public BlockStairBlocks(Block infBlock) {
        super(infBlock.getDefaultState());
        
        setCreativeTab(Main.tabBlocks);
        setLightOpacity(0);
        useNeighborBrightness = true;
        modelBlock = infBlock;
        
        if(infBlock == Blocks.ICE || infBlock == Blocks.PACKED_ICE) {
            setDefaultSlipperiness(0.98F);
            setTickRandomly(true);
        }
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face){
        return modelBlock == Blocks.ICE || modelBlock == Blocks.GLASS ? false : super.doesSideBlockRendering(state, world, pos, face);
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        if(modelBlock == Blocks.ICE){
            if(world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - state.getLightOpacity(world, pos)){
                if(world.provider.doesWaterVaporize()){
                    world.setBlockToAir(pos);
                }else{
                    dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
                    world.setBlockState(pos, Blocks.WATER.getDefaultState());
                    world.neighborChanged(pos, Blocks.WATER, pos);
                }
            }
        }
    }
}