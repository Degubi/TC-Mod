package degubi.teamcraft.entity;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class EntitySkeletonSpider extends EntitySpider {

    public EntitySkeletonSpider(World world) {
        super(world);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if(super.attackEntityAsMob(entity)) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
            }

            return true;
        }

        return false;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int weight) {
        if(wasRecentlyHit && (rand.nextInt(3) == 0 || rand.nextInt(1 + weight) > 0)) {
            entityDropItem(new ItemStack(Items.COAL, 1, 1), 100);
        }

        if(rand.nextInt(1000) == 5) {
            entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.15F, 1.0F);
    }
}