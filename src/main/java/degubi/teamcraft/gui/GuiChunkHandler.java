package degubi.teamcraft.gui;

import java.io.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.*;
import net.minecraftforge.fml.relauncher.*;
import org.apache.commons.lang3.*;

@SideOnly(Side.CLIENT)
public class GuiChunkHandler extends GuiScreen {
    private static final String title = I18n.format("string.chunkhandler") + ": ";

    private GuiTextField commandTextField;
    private final GuiScreen parent;

    public GuiChunkHandler(GuiScreen last){
        parent = last;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 25, 200, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(1, width / 2 + 70, 60, 80, 20, I18n.format("string.increase")));
        buttonList.add(new GuiButton(2, width / 2 - 150, 60, 80, 20, I18n.format("string.decrease")));
        commandTextField = new GuiTextField(2, fontRenderer, width / 2 - 15, 60, 30, 20);
        commandTextField.setMaxStringLength(3);
        commandTextField.setFocused(true);
        commandTextField.setText(String.valueOf(mc.gameSettings.renderDistanceChunks));
    }

    @Override
    protected void actionPerformed(GuiButton button){
        if(button.enabled){
            if(button.id == 1){
                mc.gameSettings.renderDistanceChunks++;
                commandTextField.setText(String.valueOf(mc.gameSettings.renderDistanceChunks));
            }else if(button.id == 2){
                if(mc.gameSettings.renderDistanceChunks != 0){
                    mc.gameSettings.renderDistanceChunks--;
                    commandTextField.setText(String.valueOf(mc.gameSettings.renderDistanceChunks));
                }
            }else{
                if(StringUtils.isNumeric(commandTextField.getText()) && Integer.parseInt(commandTextField.getText()) > 0){
                    mc.gameSettings.renderDistanceChunks = Integer.parseInt(commandTextField.getText());
                }
                mc.displayGuiScreen(parent);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        super.keyTyped(typedChar, keyCode);
        commandTextField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen(){
        commandTextField.updateCursorCounter();
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawDefaultBackground();
        drawGradientRect(width / 2 - 90, 16, width / 2 + 90, 32, -1072689136, -804253680);
        drawCenteredString(fontRenderer, title + mc.gameSettings.renderDistanceChunks, width / 2, 20, 0xffffff);
        commandTextField.drawTextBox();

        for(GuiButton buttons : buttonList){
            buttons.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }
}