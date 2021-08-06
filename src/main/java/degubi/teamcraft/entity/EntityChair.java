package degubi.teamcraft.entity;

import degubi.teamcraft.block.interactive.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class EntityChair extends Entity{

    public EntityChair(World theWorld){
        super(theWorld);
        setSize(0.5F, 0.5F);
    }

    public EntityChair(World theWorld, double x, double y, double z){
        this(theWorld);
        setPosition(x, y, z);
    }

    @Override
    protected void entityInit(){}

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double X, double Y, double Z, float yaw, float pitch, int posRotationIncrements, boolean teleport){
        setPosition(X, Y, Z);
        setRotation(yaw, pitch);
    }

    @Override
    public void onUpdate(){
        super.onUpdate();

        if(!world.isRemote && !isBeingRidden()){
            world.setBlockState(getPosition(), world.getBlockState(getPosition()).withProperty(BlockChair.isOccupied, Boolean.FALSE));
            setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag){}
    @Override
    protected void readEntityFromNBT(NBTTagCompound tag){}
}