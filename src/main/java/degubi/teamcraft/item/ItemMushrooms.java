package degubi.teamcraft.item;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class ItemMushrooms extends ItemFood {
    
    public ItemMushrooms(int amount) {
        super(amount, false);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack){
        return 100;
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!world.isRemote && facing == EnumFacing.UP){
            ItemStack stack = player.getHeldItem(hand);
            Block place = stack.getItem() == Main.RedMushroomItem ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM;
            
            if(place.canPlaceBlockAt(world, pos.up())){
                world.setBlockState(pos.up(), place.getDefaultState());
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}