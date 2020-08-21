package degubi.teamcraft.item;

import degubi.teamcraft.block.interactive.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class ItemRope extends ItemBlock {
    
    public ItemRope() {
        super(new BlockRope());
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            IBlockState setState = block.getDefaultState().withProperty(BlockLadder.FACING, facing);
            ItemStack heldItem = player.getHeldItem(hand);
            int currentSize = heldItem.getCount();
            int useCounter = 0;
            BlockPos downPos = pos.offset(facing, 1);
            
            for(int down = 0; down < currentSize; ++down) {
                if(world.getBlockState(downPos.down(down)).getBlock() == Blocks.AIR){
                    world.setBlockState(downPos.down(down), setState, 2);
                    useCounter++;
                }else{
                    break;
                }
            }
            
            heldItem.shrink(useCounter);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}