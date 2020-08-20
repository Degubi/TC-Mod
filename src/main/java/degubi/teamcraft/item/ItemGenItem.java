package degubi.teamcraft.item;

import degubi.teamcraft.*;
import degubi.teamcraft.gui.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class ItemGenItem extends Item{
    
    public ItemGenItem(){
        setCreativeTab(Main.tabAdmin);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
        if(player.isSneaking()){
            Minecraft.getMinecraft().displayGuiScreen(new GuiNBTEdit(stack));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setIntArray("clickPos0", new int[]{pos.getX(), pos.getY() + 1, pos.getZ()});
            StructureHelper.setBlocksfromFile(world, pos.up(), player.getHeldItem(hand).getDisplayName(), tag, player);
        }
        return EnumActionResult.SUCCESS;
    }
}