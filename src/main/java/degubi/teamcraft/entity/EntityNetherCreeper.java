package degubi.teamcraft.entity;

import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public final class EntityNetherCreeper extends EntityCreeper{
    private int timeSinceIgnited;
    private int time = 15;
    private int radius = 6;

    public EntityNetherCreeper(World theWorld){
        super(theWorld);
        isImmuneToFire = true;
    }

    @Override
    protected Item getDropItem(){
        return Items.MAGMA_CREAM;
    }

    @Override
    public void onUpdate(){
        if(hasIgnited()){
            setCreeperState(1);
        }

        int i = getCreeperState();
        if (i > 0 && timeSinceIgnited == 0){
            playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
        }

        timeSinceIgnited += i;
        if (timeSinceIgnited < 0){
            timeSinceIgnited = 0;
        }

        if (timeSinceIgnited >= time){
            timeSinceIgnited = time;
            makeExplosion();
        }

        if(isInWater()){
            time = 0;
            radius = 20;
            makeExplosion();
        }
        super.onUpdate();
    }

    private void makeExplosion(){
        if(!world.isRemote){
            world.createExplosion(this, posX, posY, posZ, radius, world.getGameRules().getBoolean("mobGriefing"));
            setDead();
        }
    }
}