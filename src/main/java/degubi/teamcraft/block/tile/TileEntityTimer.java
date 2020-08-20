package degubi.teamcraft.block.tile;

import degubi.teamcraft.block.interactive.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class TileEntityTimer extends TileEntity implements ITickable{
    private int counter, maxCounter;
    private boolean isActive;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        counter = tag.getInteger("counter");
        maxCounter = tag.getInteger("maxCounter");
        isActive = tag.getBoolean("isActive");
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("counter", counter);
        tag.setInteger("maxCounter", maxCounter);
        tag.setBoolean("isActive", isActive);
        return tag;
    }
    @Override
    public void update() {
        if(!world.isRemote && isActive){
            counter++;
            
            if(counter >= maxCounter){
                counter = 0;
            }
            if(counter == 0){
                world.setBlockState(getPos(), world.getBlockState(getPos()).cycleProperty(BlockRedstoneTimer.isPowered));
                world.scheduleUpdate(getPos(), getBlockType(), -1);
            }
        }
    }
    @Override
    public boolean shouldRefresh(World theWorld, BlockPos blockPos, IBlockState oldState, IBlockState newSate) {
        return newSate.getBlock() == Blocks.AIR;
    }
    public void switchActiveState(){
        if(isActive){
            world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(BlockRedstoneTimer.isPowered, Boolean.FALSE));
            isActive = false;
        }else{
            isActive = true;
        }
    }
    public void incMaxCounter() {
        maxCounter++;
        
        if(maxCounter >= 20) {
            maxCounter = 20;
        }
    }
    public void decMaxCounter() {
        maxCounter--;
        
        if(maxCounter <= 0) {
            maxCounter = 0;
        }
    }
}