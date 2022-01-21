package degubi.teamcraft.entity;

import degubi.teamcraft.entity.ai.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.storage.loot.*;

public final class EntityThreeHeadCreeper extends EntityMob {
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(EntityThreeHeadCreeper.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityThreeHeadCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(EntityThreeHeadCreeper.class, DataSerializers.BOOLEAN);

    public int lastActiveTime;
    public int timeSinceIgnited;
    public int fuseTime = 100;
    private int explosionRadius = 10;

    public EntityThreeHeadCreeper(World world) {
        super(world);

        this.setSize(0.6F, 1.7F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwellExt(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    public int getMaxFallHeight() {
        return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);

        this.timeSinceIgnited = (int)(this.timeSinceIgnited + distance * 1.5F);

        if(this.timeSinceIgnited > this.fuseTime - 5) {
            this.timeSinceIgnited = this.fuseTime - 5;
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        this.dataManager.register(STATE, Integer.valueOf(-1));
        this.dataManager.register(POWERED, Boolean.FALSE);
        this.dataManager.register(IGNITED, Boolean.FALSE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if(this.dataManager.get(POWERED).booleanValue()) {
            compound.setBoolean("powered", true);
        }

        compound.setShort("Fuse", (short)this.fuseTime);
        compound.setByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.setBoolean("ignited", this.dataManager.get(IGNITED).booleanValue());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.dataManager.set(POWERED, Boolean.valueOf(compound.getBoolean("powered")));

        if(compound.hasKey("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if(compound.hasKey("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if(compound.getBoolean("ignited")) {
            this.dataManager.set(IGNITED, Boolean.TRUE);
        }
    }

    @Override
    public void onUpdate() {
        if(this.isEntityAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;

            if(this.dataManager.get(IGNITED).booleanValue()) {
                this.setCreeperState(1);
            }

            int creeperState = this.getCreeperState();
            if(creeperState > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += creeperState;
            if(this.timeSinceIgnited < 0){
                this.timeSinceIgnited = 0;
            }

            if(this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;

                if (!this.world.isRemote) {
                    float explosionRadiusMultiplier = this.dataManager.get(POWERED).booleanValue() ? 2.0F : 1.0F;
                    boolean isGriefingEnabled = this.world.getGameRules().getBoolean("mobGriefing");

                    this.dead = true;
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * explosionRadiusMultiplier, isGriefingEnabled);
                    this.setDead();
                }
            }

            if(isBurning()) {
                fuseTime = 0;
                explosionRadius = 15;
            }
        }

        super.onUpdate();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return true;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_CREEPER;
    }

    public int getCreeperState() {
        return this.dataManager.get(STATE).intValue();
    }

    public void setCreeperState(int state) {
        this.dataManager.set(STATE, Integer.valueOf(state));
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        super.onStruckByLightning(lightningBolt);

        this.dataManager.set(POWERED, Boolean.TRUE);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if(!stack.isEmpty() && stack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(player, this.posX, this.posY, this.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            player.swingArm(hand);

            if(!this.world.isRemote) {
                this.dataManager.set(IGNITED, Boolean.TRUE);
                stack.damageItem(1, player);
                return true;
            }
        }

        return super.processInteract(player, hand);
    }
}