package degubi.teamcraft.entity;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.pathfinding.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class EntityCowBrine extends EntityMob{
    
    public EntityCowBrine(World theWorld){
        super(theWorld);
        setSize(0.9F, 1.3F);
        setPathPriority(PathNodeType.WATER, -1.0F);
        tasks.addTask(1, new EntityAIWander(this, 1.0D));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }
    
    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute(){
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    protected SoundEvent getAmbientSound(){
        return SoundEvents.ENTITY_COW_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource){
        return SoundEvents.ENTITY_COW_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.ENTITY_COW_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn){
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }
    
    @Override
    protected float getSoundVolume(){
        return 0.4F;
    }
    
    @Override
    protected Item getDropItem(){
        return Items.ROTTEN_FLESH;
    }
}