package degubi.teamcraft.block.tile;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class TableContainer extends ContainerWorkbench{
    private final int posX;
    private final int posY;
    private final int posZ;

    public TableContainer(InventoryPlayer inventory, World world, int x, int y, int z){
        super(inventory, world, new BlockPos(x, y, z));
        
        posX = x;
        posY = y;
        posZ = z;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player){
        return player.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) > 64.0D;
    }
}