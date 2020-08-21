package degubi.teamcraft.item;

import degubi.teamcraft.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public final class ItemKnife extends ItemSword {
    private static final PotionEffect poti = new PotionEffect(MobEffects.SPEED, 1, 0);

    public ItemKnife(ToolMaterial material) {
        super(material);
        setCreativeTab(Main.ToolsWeapons);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIn, boolean isEquiped) {
        if(stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("soundcounter", 0);
        }
        
        NBTTagCompound nbtTag = stack.getTagCompound();
        
        if(isEquiped){
            ((EntityLivingBase)entity).addPotionEffect(poti);
            
            if(world.isRemote){
                int cc = nbtTag.getInteger("soundcounter");
                
                if (cc < 5) {
                    cc++;
                }
                
                nbtTag.setInteger("soundcounter", cc);
                if (nbtTag.getInteger("soundcounter") == 2) {
                    entity.playSound(new SoundEvent(new ResourceLocation("tcm:takeout.knife")), 1.0F, 1.0F);
                }
            }
        }else {
            if(nbtTag.getInteger("soundcounter") != 0) {
                nbtTag.setInteger("soundcounter", 0);
            }
        }
    }
}