package degubi.teamcraft.gui;

import degubi.teamcraft.*;
import degubi.teamcraft.network.*;
import java.io.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.*;

@SideOnly(Side.CLIENT)
public class GuiNBTEdit extends GuiScreen{
    private GuiTextField commandTextField;
    private final ItemStack stack;
    
    public GuiNBTEdit(ItemStack ss){
        stack = ss;
    }
    
    @Override
    public void initGui() {
        commandTextField = new GuiTextField(2, fontRenderer, width / 2 - 35, 60, 70, 20);
        commandTextField.setMaxStringLength(10);
        commandTextField.setFocused(true);
        commandTextField.setText(stack.getDisplayName());
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 25, 200, 20, "gui.done"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button){
        if(button.enabled){
            Main.nbtChannel.sendToServer(new MessageString(commandTextField.getText()));
            mc.displayGuiScreen(null);
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        super.keyTyped(typedChar, keyCode);
        commandTextField.textboxKeyTyped(typedChar, keyCode);
        if(keyCode == Keyboard.KEY_RETURN){
            Main.nbtChannel.sendToServer(new MessageString(commandTextField.getText()));
            mc.displayGuiScreen(null);
        }
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
        drawGradientRect(width / 2 - 90, 16, width / 2 + 90, 32, -1072689136, -804253680);
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Current name: " + stack.getDisplayName(), width / 2, 20, 0xffffff);
        commandTextField.drawTextBox();
        
        for(GuiButton buttons : buttonList) {
            buttons.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }
}