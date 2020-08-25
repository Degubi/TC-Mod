package degubi.teamcraft.entity;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.datasync.*;
import net.minecraft.pathfinding.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.living.*;

public final class EntityEnderSpider extends EntitySpider{
    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityEnderSpider.class, DataSerializers.BOOLEAN);
    private boolean isAggressive;

    public EntityEnderSpider(World theWorld) {
        super(theWorld);
        setPathPriority(PathNodeType.WATER, -1.0F);
    }
    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
        dataManager.register(SCREAMING, Boolean.FALSE);
    }

    @Override
    public void onLivingUpdate(){
        World world = this.world;

        if(world.isRemote){
            Random rand = this.rand;
            double posX = this.posX;
            double posY = this.posY;
            double posZ = this.posZ;
            float height = this.height;
            float width = this.width;

            world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height - 0.25D, posZ + (rand.nextDouble() - 0.5D) * width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
            world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height - 0.25D, posZ + (rand.nextDouble() - 0.5D) * width, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
        }

        isJumping = false;
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks(){
        if(isWet()){
            attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if(isScreaming() && !isAggressive && rand.nextInt(100) == 0){
            setScreaming(false);
        }

        if(world.isDaytime()){
            float f = getBrightness();

            if(f > 0.5F && world.canSeeSky(new BlockPos(this)) && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F){
                setAttackTarget((EntityLivingBase)null);
                setScreaming(false);
                isAggressive = false;
                teleportRandomly();
            }
        }
        super.updateAITasks();
    }

    private boolean teleportRandomly(){
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    private boolean teleportTo(double x, double y, double z){
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
        if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if(flag){
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            int num = rand.nextInt(25);

            if(world.getGameRules().getBoolean("mobGriefing")){
                if(num < 4){
                    world.setBlockState(new BlockPos(this.posX, this.posY, this.posZ), Blocks.WEB.getDefaultState());
                    playSound(SoundEvents.BLOCK_CLOTH_STEP, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
                if(num == 6){
                    world.setBlockState(getPosition(), Main.SpiderEgg.getDefaultState());
                }
            }
        }
        return flag;
    }

    @Override
    protected SoundEvent getAmbientSound(){
        return isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    @Override
    protected Item getDropItem(){
        return Items.ENDER_PEARL;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int weight){
        Item item = getDropItem();
        int j = rand.nextInt(2 + weight);

        for(int k = 0; k < j; ++k){
            dropItem(item, 1);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount){
        if (isEntityInvulnerable(source)){
            return false;
        }
        setScreaming(true);

        if (source instanceof EntityDamageSource && source.getTrueSource() instanceof EntityPlayer){
            isAggressive = true;
        }

        if (source instanceof EntityDamageSourceIndirect){
            isAggressive = false;

            for (int i = 0; i < 64; ++i){
                if (teleportRandomly()){
                    return true;
                }
            }
            return super.attackEntityFrom(source, amount);
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean isScreaming(){
        return this.dataManager.get(SCREAMING).booleanValue();
    }

    private void setScreaming(boolean isScreaming){
        dataManager.set(SCREAMING, Boolean.valueOf(isScreaming));
    }
}