package degubi.teamcraft.gui;

import java.io.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.settings.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class GuiTCMSettings extends GuiScreen {
    private static final String MOD_VERSION = FMLCommonHandler.instance().findContainerFor("tcm").getVersion();
    private static final String title = I18n.format("options.title");

    private final GuiScreen lastScreen;
    private final GameSettings settings;

    GuiTCMSettings(GuiScreen parent, GameSettings sett){
        lastScreen = parent;
        settings = sett;
    }
    
    @Override
    public void initGui(){
        buttonList.add(new GuiOptionSlider(GameSettings.Options.FOV.getOrdinal(), width / 2 - 155, 30, GameSettings.Options.FOV));
        buttonList.add(new GuiButton(100, width / 2 + 5, height / 6 + 72 - 6, 150, 20, I18n.format("options.controls")));
        buttonList.add(new GuiButton(101, width / 2 - 155, height / 6 + 72 - 6, 150, 20, I18n.format("options.video")));
        buttonList.add(new GuiButton(102, width / 2 - 155, height / 6 + 96 - 6, 150, 20, I18n.format("options.language")));
        buttonList.add(new GuiButton(103, width / 2 + 5, height / 6 + 96 - 6, 150, 20, I18n.format("options.chat.title")));
        buttonList.add(new GuiButton(104, width / 2 + 5, height / 6 + 120 - 6, 150, 20, I18n.format("options.snooper.view")));
        buttonList.add(new GuiButton(105, width / 2 - 155, height / 6 + 120 - 6, 150, 20, I18n.format("options.resourcepack")));
        buttonList.add(new GuiButton(106, width / 2 + 5, height / 6 + 48 - 6, 150, 20, I18n.format("options.sounds")));
        buttonList.add(new GuiButton(107, width / 2 + 5, 30, 150, 20, "Forge options"));
        buttonList.add(new GuiButton(108, width / 2 - 155, height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation")));
        buttonList.add(new GuiButton(109, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
    }
    
    @Override
    public void confirmClicked(boolean result, int id){
        mc.displayGuiScreen(this);
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(keyCode == 1){
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(lastScreen);
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        switch(button.id) {
            case 100: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiControls(this, settings)); break;
            case 101: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiVideoSettings(this, settings)); break;
            case 102: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiLanguage(this, settings, mc.getLanguageManager())); break;
            case 103: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new ScreenChatOptions(this, settings)); break;
            case 104: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiSnooper(this, settings)); break;
            case 105: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiScreenResourcePacks(this)); break;
            case 106: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiScreenOptionsSounds(this, settings)); break;
            case 107: mc.displayGuiScreen(new GuiModList(this)); break;
            case 108: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(new GuiCustomizeSkin(this)); break;
            case 109: mc.gameSettings.saveOptions();
                      mc.displayGuiScreen(lastScreen); break;
            default:  if(button instanceof GuiOptionButton){
                GameSettings.Options gamesettings$options = ((GuiOptionButton)button).getOption();
                settings.setOptionValue(gamesettings$options, 1);
                button.displayString = settings.getKeyBinding(GameSettings.Options.byOrdinal(button.id));
            }
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 15, 16777215);
        drawCenteredString(fontRenderer, "Mod version: " + MOD_VERSION, width - 50, height - 12, 16777215);
        
        for(GuiButton buttons : buttonList) {
            buttons.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }
}