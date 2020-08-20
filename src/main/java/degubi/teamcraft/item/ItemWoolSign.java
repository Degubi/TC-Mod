package degubi.teamcraft.item;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.block.vanilla.*;
import net.minecraft.advancements.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class ItemWoolSign extends ItemBlock {
    
    public ItemWoolSign() {
        super(new WoolSSign());
        setMaxStackSize(16);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        IBlockState iblockstate = world.getBlockState(pos);
        boolean flag = iblockstate.getBlock().isReplaceable(world, pos);
        
        if (facing != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || facing == EnumFacing.UP)){
            BlockPos noPos = pos;
            noPos = pos.offset(facing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(noPos, facing, itemstack) && Main.WoolStandingSign.canPlaceBlockAt(world, noPos)){
                if (world.isRemote){
                    return EnumActionResult.SUCCESS;
                }
                noPos = flag ? noPos.down() : noPos;

                if (facing == EnumFacing.UP){
                    world.setBlockState(noPos, Main.WoolStandingSign.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(MathHelper.floor((player.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15)), 11);
                }else{
                    world.setBlockState(noPos, Main.WoolWallSign.getDefaultState().withProperty(BlockWallSign.FACING, facing), 11);
                }
                
                TileEntity tileentity = world.getTileEntity(noPos);
                if (tileentity instanceof TileEntityWoolSign && !ItemBlock.setTileEntityNBT(world, player, noPos, itemstack)){
                    player.openEditSign((TileEntityWoolSign)tileentity);
                }
                if (player instanceof EntityPlayerMP){
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, noPos, itemstack);
                }
                
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.FAIL;
    }
        /*IBlockState iblockstate = world.getBlockState(pos);
        boolean flag = iblockstate.getBlock().isReplaceable(world, pos);
        if (facing != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || facing == EnumFacing.UP)){
            BlockPos thePos = pos.offset(facing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(thePos, facing, itemstack) && Main.WoolStandingSign.canPlaceBlockAt(world, thePos)){
                if (world.isRemote){
                    return EnumActionResult.SUCCESS;
                }
                thePos = flag ? thePos.down() : thePos;

                if (facing == EnumFacing.UP){
                    int i = MathHelper.floor((player.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
                    world.setBlockState(thePos, Main.WoolStandingSign.getDefaultState().withProperty(WoolSSign.ROTATION, Integer.valueOf(i)), 11);
                }
                else{
                    world.setBlockState(pos, Main.WoolWallSign.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 11);
                }

                TileEntity tileentity = world.getTileEntity(pos);

                if (tileentity instanceof WoolSignTile && !ItemBlock.setTileEntityNBT(world, player, pos, itemstack)){
                    player.openEditSign((WoolSignTile)tileentity);
                }
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.FAIL;
        */
}