package degubi.teamcraft.gui;

import degubi.teamcraft.*;
import degubi.teamcraft.network.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiRedstoneTimer extends GuiScreen {
    private final BlockPos blockPos;

    public GuiRedstoneTimer(BlockPos pos) {
        blockPos = pos;
    }

    @Override
    public void initGui() {
        addButton(new GuiButton(0, width / 2 - 50, 60, 100, 20, "Enable/Disable"));
        addButton(new GuiButton(1, width / 2 - 50, 120, 100, 20, I18n.format("string.increase")));
        addButton(new GuiButton(2, width / 2 - 50, 160, 100, 20, I18n.format("string.decrease")));
    }

    @Override
    protected void actionPerformed(GuiButton button){
        if(button.enabled){
            if(button.id == 0){
                Main.redstoneTimerChannel.sendToServer(new MessageCoordInt(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0));
            }else if(button.id == 1){
                Main.redstoneTimerChannel.sendToServer(new MessageCoordInt(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1));
            }else if(button.id == 2){
                Main.redstoneTimerChannel.sendToServer(new MessageCoordInt(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2));
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Redstone Timer", width / 2, 20, 0xffffff);

        for(GuiButton buttons : buttonList) {
            buttons.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }
}