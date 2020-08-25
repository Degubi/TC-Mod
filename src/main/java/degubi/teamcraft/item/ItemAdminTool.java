package degubi.teamcraft.item;

import degubi.teamcraft.*;
import degubi.teamcraft.entity.*;
import java.util.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class ItemAdminTool extends Item {

    public ItemAdminTool(){
        setCreativeTab(Main.tabAdmin);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn) {
        list.add("Right click to Goat to change skin,");
        list.add("or right click to Conveyor to change direction");
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!target.world.isRemote && target instanceof EntityGoat){
            EntityGoat goat = (EntityGoat) target;
            int type = goat.getGoatType();

            goat.setGoatType(type == 3 ? 0 : ++type);
        }
        return true;
    }
}