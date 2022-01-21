package degubi.teamcraft.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public final class EntityPigSpider extends EntitySpider {
    private static final DataParameter<Boolean> isNetherSpider = EntityDataManager.createKey(EntityPigSpider.class, DataSerializers.BOOLEAN);

    private int webCounter;

    public EntityPigSpider(World world) {
        super(world);

        if(world.provider.isNether()) {
            setIsNetherSpider(true);
            webCounter = 200 + rand.nextInt(500);
        }else{
            setIsNetherSpider(false);
        }
    }

    @Override
    public void onLivingUpdate() {
        if(isNetherSpider() && !world.isRemote) {
            --webCounter;

            if(webCounter <= 30 && getAttackTarget() != null) {
                playSound(SoundEvents.BLOCK_CLOTH_PLACE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                world.setBlockState(getPosition(), Blocks.WEB.getDefaultState());
                webCounter = 200 + rand.nextInt(500);
            }
        }

        super.onLivingUpdate();
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        dataManager.register(isNetherSpider, Boolean.FALSE);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setInteger("webCounter", webCounter);
        tag.setBoolean("isNether", isNetherSpider());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        webCounter = tag.getInteger("webCounter");
        if(tag.hasKey("isNether")) {
            setIsNetherSpider(tag.getBoolean("isNether"));
        }
    }

    public boolean isNetherSpider() {
        return dataManager.get(isNetherSpider).booleanValue();
    }

    private void setIsNetherSpider(boolean is) {
        if(is) {
            dataManager.set(isNetherSpider, Boolean.TRUE);
            isImmuneToFire = true;
            setSize(2F, 1.1F);
        }else{
            dataManager.set(isNetherSpider, Boolean.FALSE);
            setSize(1F, 0.7F);
        }
    }

    @Override
    public void setDead() {
        if(world.provider.isNether() && !world.isRemote) {
            if(rand.nextInt(3) == 1) {
                EntitySkeletonSpider skele = new EntitySkeletonSpider(world);
                skele.setLocationAndAngles(posX, posY + 0.5D, posZ, rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntity(skele);
            }
        }

        super.setDead();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected Item getDropItem() {
        return isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int weight) {
        super.dropFewItems(wasRecentlyHit, weight);

        if(wasRecentlyHit && (rand.nextInt(3) == 0 || rand.nextInt(1 + weight) > 0)){
            dropItem(Items.STRING, 2);
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        if(super.attackEntityAsMob(target)) {
            if(target instanceof EntityLivingBase && !world.provider.isSurfaceWorld()) {
                ((EntityLivingBase)target).addPotionEffect(new PotionEffect(MobEffects.POISON, 15 * 20, 0));
            }
            return true;
        }

        return false;
    }
}