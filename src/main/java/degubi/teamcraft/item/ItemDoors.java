package degubi.teamcraft.item;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class ItemDoors extends ItemBlock {
    
    public ItemDoors(Block theBlock){
        super(theBlock);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(facing == EnumFacing.UP){
            if(!world.getBlockState(pos).getBlock().isReplaceable(world, pos)){
                pos = pos.offset(facing);
            }
            
            ItemStack stack = player.getHeldItem(hand);
            
            if(player.canPlayerEdit(pos, facing, stack) && this.block.canPlaceBlockAt(world, pos)){
                EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
                int i = enumfacing.getFrontOffsetX();
                int j = enumfacing.getFrontOffsetZ();
                boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
                
                ItemDoor.placeDoor(world, pos, enumfacing, this.block, flag);
                SoundType soundtype = block.getSoundType(null, null, null, null);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}