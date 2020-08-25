package degubi.teamcraft.entity.ai;

import degubi.teamcraft.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;

public final class EntityAICreeperSwellExt extends EntityAIBase{
    private final EntityThreeHeadCreeper swellingCreeper;
    private EntityLivingBase creeperAttackTarget;

    public EntityAICreeperSwellExt(EntityThreeHeadCreeper creepy){
        swellingCreeper = creepy;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute(){
        return swellingCreeper.getCreeperState() > 0 || swellingCreeper.getAttackTarget() != null && swellingCreeper.getDistance(swellingCreeper.getAttackTarget()) < 9.0D;
    }

    @Override
    public void startExecuting(){
        swellingCreeper.getNavigator().clearPath();
        creeperAttackTarget = swellingCreeper.getAttackTarget();
    }

    @Override
    public void resetTask(){
        creeperAttackTarget = null;
    }

    @Override
    public void updateTask(){
        if(creeperAttackTarget == null){
            swellingCreeper.setCreeperState(-1);
        }else if(swellingCreeper.getDistance(creeperAttackTarget) > 49.0D){
            swellingCreeper.setCreeperState(-1);
        }else if(!swellingCreeper.getEntitySenses().canSee(creeperAttackTarget)){
            swellingCreeper.setCreeperState(-1);
        }else{
            swellingCreeper.setCreeperState(1);
        }
    }
}