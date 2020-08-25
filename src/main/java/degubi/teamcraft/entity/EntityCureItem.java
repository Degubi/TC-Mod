package degubi.teamcraft.entity;

import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class EntityCureItem extends EntityThrowable{

    public EntityCureItem(World theWorld){
        super(theWorld);
    }

    public EntityCureItem(World theWorld, EntityLivingBase thrwr){
        super(theWorld, thrwr);
    }

    public EntityCureItem(World theWorld, double X, double Y, double Z){
        super(theWorld, X, Y, Z);
    }

    @Override
    protected void onImpact(RayTraceResult pos){
        if(pos.entityHit != null){
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, pos.entityHit), 1.0F);
        }

        AxisAlignedBB boundingBox = getEntityBoundingBox();
        List<EntityPlayer> entity = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(boundingBox.minX - 5D, boundingBox.minY - 5D, boundingBox.minZ - 5D, boundingBox.maxX + 5D, boundingBox.maxY + 5D, boundingBox.maxZ + 5D));
        for(EntityPlayer ent : entity){
            ent.clearActivePotions();
        }

        if (!world.isRemote){
            world.playEvent(2002, getPosition(), 8194);
            setDead();
        }
    }

    @Override
    protected float getGravityVelocity(){
        return 0.05F;
    }
}