package degubi.teamcraft.entity;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class EntityGoat extends EntityCow {
    private static final DataParameter<Integer> HornColor = EntityDataManager.createKey(EntityGoat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GoatType = EntityDataManager.createKey(EntityGoat.class, DataSerializers.VARINT);

    private EntityAIEatGrass entityAIEatGrass;
    public int sheepTimer;

    public EntityGoat(World theWorld){
        super(theWorld);

        setSize(0.875F, 1.3F);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(5, this.entityAIEatGrass = new EntityAIEatGrass(this));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(this.world.isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if(id == 10) {
            this.sheepTimer = 40;
        }else{
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();

        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
    }

    @Override
    protected void entityInit() {
        super.entityInit();

        dataManager.register(HornColor, Integer.valueOf(0));
        dataManager.register(GoatType, Integer.valueOf(0));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2000000029802322D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setInteger("Type", dataManager.get(GoatType).intValue());
        tag.setInteger("Horn", dataManager.get(HornColor).intValue());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        dataManager.set(HornColor, Integer.valueOf(tag.getInteger("Horn")));
        dataManager.set(GoatType, Integer.valueOf(tag.getInteger("Type")));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Main.ENTITY_GOAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return Main.ENTITY_GOAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Main.ENTITY_GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void dropFewItems(boolean gotHit, int weight) {
        int rng = rand.nextInt(3) + rand.nextInt(1 + weight);

        for(int j = 0; j < rng; ++j) {
            dropItem(Items.LEATHER, 1);
        }

        Item toDrop = isBurning() ? Main.CookedGoatMeat : Main.RawGoatMeat;
        for(int j = 0; j < rng; ++j) {
            dropItem(toDrop, j);
        }
    }

    public int getGoatType() {
        return dataManager.get(GoatType).intValue();
    }

    // 0: Regular, 1: Mountain, 2: Snowy
    public EntityGoat setGoatType(int type) {
        dataManager.set(GoatType, Integer.valueOf(type));
        return this;
    }

    public int getHornColor() {
        return dataManager.get(HornColor).intValue();
    }

    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public EntityGoat createChild(EntityAgeable parent) {
        return new EntityGoat(world).setGoatType(parent.getDataManager().get(GoatType).intValue());
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
        super.onInitialSpawn(difficulty, data);

        int typeToSet = world.getBiome(getPosition()).getDefaultTemperature() < 0.3F ? 2 : rand.nextInt(2);

        setGoatType(typeToSet);
        dataManager.set(HornColor, Integer.valueOf(rand.nextInt(2)));
        return data;
    }
}