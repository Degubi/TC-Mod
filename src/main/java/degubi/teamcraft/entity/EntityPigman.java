package degubi.teamcraft.entity;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class EntityPigman extends EntityMob implements IRangedAttackMob{
    private final EntityAIAttackRangedBow<EntityPigman> aiArrowAttack = new EntityAIAttackRangedBow<>(this, 1.0D, 5, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);

    public EntityPigman(World theWorld){
        super(theWorld);
        setSize(0.7F, 1.95F);

        if(!this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);

            if(getHeldItemMainhand().getItem() == Items.BOW) {
                this.aiArrowAttack.setAttackCooldown(20);
                this.tasks.addTask(4, this.aiArrowAttack);
            }else{
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(2, new EntityAIBreakDoor(this));
        tasks.addTask(2, new EntityAISwimming(this));
        tasks.addTask(3, new EntityAIOpenDoor(this, true));
        tasks.addTask(4, new EntityAIWander(this, 1.0D));
        tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();

        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
    }

    @Override
    protected SoundEvent getAmbientSound(){
        return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource){
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block){
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    public double getYOffset(){
        return super.getYOffset() - 0.4D;
    }

    @Override
    protected Item getDropItem(){
        return Items.ROTTEN_FLESH;
    }

    @Override
    protected void dropFewItems(boolean idk, int weight){
        int i = rand.nextInt(3) + 1 + rand.nextInt(1 + weight);

        for(int j = 0; j < i; ++j) {
            if(isBurning()){
                dropItem(Items.COOKED_PORKCHOP, 1);
            }else{
                dropItem(Items.PORKCHOP, 1);
                dropItem(Items.LEATHER, 1);
            }
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data){
        int i = rand.nextInt(10);

        if(i < 4){
            this.tasks.addTask(1, this.aiArrowAttack);
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }else if(i == 5){
            this.tasks.addTask(1, this.aiAttackOnCollide);
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Main.MultiTool));
        }else{
            this.tasks.addTask(1, this.aiAttackOnCollide);
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }

        if(rand.nextInt(25) < 5) {
            setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Main.EmeraldChestPlate));
        }else{
            setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Main.EmeraldHelmet));
        }
        return data;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        if(!world.isRemote) {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, EntityXPOrb.getXPSplit(50)));
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float dmg) {
        EntityArrow entityarrow = new EntityTippedArrow(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + target.height / 3.0F - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        int powerEnchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
        int punchEnchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
        int difficultyID = world.getDifficulty().getDifficultyId();

        entityarrow.setDamage(dmg * 2.0F + this.rand.nextGaussian() * 0.25D + difficultyID * 0.11F);
        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - difficultyID * 4);

        if(powerEnchantmentLevel > 0){
            entityarrow.setDamage(entityarrow.getDamage() + powerEnchantmentLevel * 0.5D + 0.5D);
        }

        if(punchEnchantmentLevel > 0){
            entityarrow.setKnockbackStrength(punchEnchantmentLevel);
        }

        playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        world.spawnEntity(entityarrow);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {}
}