package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockRope extends BlockLadder{

    public BlockRope() {
        setCreativeTab(Main.tabDecorations);
        setSoundType(SoundType.WOOD);
        setHardness(0.75F);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block changedBlock, BlockPos fromPos){
        if(world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }
}