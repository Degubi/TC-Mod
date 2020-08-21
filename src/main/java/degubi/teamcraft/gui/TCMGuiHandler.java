package degubi.teamcraft.gui;

import degubi.teamcraft.block.tile.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.relauncher.*;

public final class TCMGuiHandler implements IGuiHandler {
    
    public TCMGuiHandler() {
        super();
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new TableContainer(player.inventory, world, x, y, z);
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiTable(player.inventory, world);
    }
    
    @SideOnly(Side.CLIENT) 
    private static final class GuiTable extends GuiCrafting {
        
        GuiTable(InventoryPlayer inventory, World world) {
            super(inventory, world);
        }
        
        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
            fontRenderer.drawString(I18n.format("TeamCraft"), 110, 15, 4210752);
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        }
    }
}