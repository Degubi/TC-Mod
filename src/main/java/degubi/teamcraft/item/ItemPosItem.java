package degubi.teamcraft.item;

import degubi.teamcraft.*;
import degubi.teamcraft.gui.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class ItemPosItem extends Item {

    public ItemPosItem() {
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
            ItemStack stack = player.getHeldItem(hand);

            if(stack.hasTagCompound()){
                stack.getTagCompound().setIntArray("pos2", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendMessage(new TextComponentString("Position 2 set! " + pos.getX() + " " + pos.getY() + " " + pos.getZ()));
            }else{
                stack.setTagCompound(new NBTTagCompound());
                player.sendMessage(new TextComponentString("NBT Tag Created!"));
                stack.getTagCompound().setIntArray("pos2", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendMessage(new TextComponentString("Position 2 set! " + pos.getX() + " " + pos.getY() + " " + pos.getZ()));
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        NBTTagCompound tagCompound = stack.getTagCompound();

        if(tagCompound != null){
            if(tagCompound.hasKey("pos1")){
                int[] pos1 = tagCompound.getIntArray("pos1");
                list.add("Pos1: " + pos1[0] + " " + pos1[1] + " " + pos1[2]);
            }
            if(tagCompound.hasKey("pos2")){
                int[] pos2 = tagCompound.getIntArray("pos2");
                list.add("Pos2: " + pos2[0] + " " + pos2[1] + " " + pos2[2]);
            }
            if(tagCompound.hasKey("Blocks")){
                list.add("Blocks copied: " + tagCompound.getInteger("Blocks"));
            }
        }
    }
}