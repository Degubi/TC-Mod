package degubi.teamcraft.gui;

import com.mojang.realmsclient.gui.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.*;
import org.apache.commons.io.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;

@SideOnly(Side.CLIENT)
public final class GuiTCMMain extends GuiScreen {
    private static String splashText = "No net";
    private static final ResourceLocation gear = new ResourceLocation("tcm:textures/gui/settings.png");
    private static final BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
    private static final ResourceLocation mainMenuLogo = new ResourceLocation("tcm:textures/gui/tclogo.png");
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("tcm:textures/gui/menu/panorama_0.jpg"), new ResourceLocation("tcm:textures/gui/menu/panorama_1.jpg"), new ResourceLocation("tcm:textures/gui/menu/panorama_2.jpg"), new ResourceLocation("tcm:textures/gui/menu/panorama_3.jpg"), new ResourceLocation("tcm:textures/gui/menu/panorama_4.jpg"), new ResourceLocation("tcm:textures/gui/menu/panorama_5.jpg")};
    private static long lastClicked;
    private static ResourceLocation backgroundTexture;
    private static int panoTimer;

    @Override
    public boolean doesGuiPauseGame(){return false;}
    @Override
    protected void keyTyped(char character, int keyCode) {}

    @Override
    public void initGui(){
        super.initGui();
        
        if(backgroundTexture == null) {
            backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", new DynamicTexture(512, 512));
        }
        
        buttonList.add(new GuiButton(0, width / 2 - 110, height / 4 + 20, 100, 20, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButton(1, width / 2 - 110, height / 4 + 50, 100, 20, I18n.format("menu.multiplayer")));
        buttonList.add(new GuiButton(2, width - 20, height - 20, 20, 20, ""));
        buttonList.add(new GuiButton(3, width / 2 + 10, height / 4 + 20, 100, 20, I18n.format("string.modopts")));
        buttonList.add(new GuiButton(5, width / 2 - 100, height - 30, 200, 20, I18n.format("menu.quit")));
        buttonList.add(new GuiButton(20, 0, height - 15, 80, 15, ""));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        switch(button.id) {
            case 0: mc.displayGuiScreen(new GuiWorldSelection(this)); break;
            case 1: mc.displayGuiScreen(new GuiMultiplayer(this)); break;
            case 2: mc.displayGuiScreen(new GuiTCMSettings(this, mc.gameSettings)); break;
            case 3:
                GuiScreen newScreen = FMLClientHandler.instance().getGuiFactoryFor(FMLCommonHandler.instance().findContainerFor("tcm")).createConfigGui(this);
                mc.displayGuiScreen(newScreen); break;
            case 5: mc.shutdown(); break;
            default: openTeamCraftWeb();
        }
    }
    
    protected static void openTeamCraftWeb(){
        if(Minecraft.getSystemTime() - lastClicked < 250L) { //isDoubleClick
            openWebpage("https://team-craft.eu/");
        }
        lastClicked = Minecraft.getSystemTime();
    }
    
    public static void openWebpage(String topass){
        try{
            Desktop.getDesktop().browse(new URI(topass));
        }catch(IOException | URISyntaxException e){
            System.out.println("Couldn't open " + topass);
        }
    }
    
    @Override
    public void updateScreen(){
        ++panoTimer;
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        // Background
        mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 512, 512);
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //eredeti 64
        for(int j = 0; j < 32; ++j){
            GlStateManager.pushMatrix();
            float f = ((float)(j % 8) / (float)8 - 0.5F) / 64.0F;
            float f1 = ((float)(j / 8) / (float)8 - 0.5F) / 64.0F;
            GlStateManager.translate(f, f1, 0.0F);
            GlStateManager.rotate(MathHelper.sin((panoTimer + partialTicks) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-(panoTimer + partialTicks) * 0.1F, 0.0F, 1.0F, 0.0F);
            for(int k = 0; k < 6; ++k){
                GlStateManager.pushMatrix();
                if (k == 1){
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }else if (k == 2){
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }else if (k == 3){
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }else if (k == 4){
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }else if (k == 5){
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }
                mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                int l = 255 / (j + 1);
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldRenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldRenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldRenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                Tessellator.getInstance().draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        
        mc.getTextureManager().bindTexture(backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        for(int k = 0; k < 5; ++k) {
            GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 512, 512);
            GlStateManager.colorMask(true, true, true, false);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            GlStateManager.disableAlpha();

            for(int j = 0; j < 3; ++j){
                float f = 1.0F / (j + 1);
                float f1 = (j - 3 / 2) / 256.0F;
                worldRenderer.pos(width, height, zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
                worldRenderer.pos(width, 0.0D, zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
                worldRenderer.pos(0.0D, 0.0D, zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
                worldRenderer.pos(0.0D, height, zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            }
            Tessellator.getInstance().draw();
            GlStateManager.enableAlpha();
            GlStateManager.colorMask(true, true, true, true);
        }
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        float f = width > height ? 110.0F / width : 110.0F / height;
        float f1 = height * f / 256.0F;
        float f2 = width * f / 256.0F;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(0.0D, height, zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(width, height, zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(width, 0.0D, zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(0.0D, 0.0D, zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        Tessellator.getInstance().draw();
        drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
        
        //Főlogó
        mc.getTextureManager().bindTexture(mainMenuLogo);
        GL11.glPushMatrix();
        GlStateManager.color(0.9F, 0.9F, 0.9F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glScalef(0.9F, 0.9F, 0.9F);
        drawTexturedModalRect(width / 2 - 90, 10, 0, 0, 256, 44);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
        
        //Gombok
        for(GuiButton button : buttonList){
            if(button.id != 2 && button.id != 20) {
                button.drawButton(mc, mouseX, mouseY, partialTicks);
            }
        }
        
        //TeamCraft Link
        if(mouseX < 80 && mouseY > height - 15) {
            drawString(fontRenderer, "team-craft.eu" + ChatFormatting.UNDERLINE, 7, height - 12, 0x0645AD);
        }else{
            drawString(fontRenderer, "team-craft.eu" + ChatFormatting.UNDERLINE, 7, height - 12, 0x3366BB);
        }
        
        //Splashek
        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2 + 80, 37F, 0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float timer = 1.3F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
        timer *= 100.0F / (fontRenderer.getStringWidth(splashText) + 32);
        GlStateManager.scale(timer, timer, timer);
        drawCenteredString(fontRenderer, splashText + ChatFormatting.UNDERLINE, 0, 0, -254);
        GlStateManager.popMatrix();
        
        //Settings
        mc.renderEngine.bindTexture(gear);
        GL11.glPushMatrix();
        GL11.glColor4f(0.75F, 0.75F, 0.75F, 1F);
        GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
        drawTexturedModalRect(width * 16 - 300, height * 16 - 300, 0, 0, 256, 256);
        GL11.glPopMatrix();
    }
    
    private static final java.util.List<String> readFromURL(String url) {
        try(CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(new HttpGet(url));
            InputStream content = response.getEntity().getContent()) {
            
            return IOUtils.readLines(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Couldn't load " + url);
            return new ArrayList<>();
        }
    }
    
    public static void loadGuiResources(){
        LocalDate date = LocalDate.now();
        
        if(date.getMonth() == Month.JUNE && date.getDayOfMonth() == 1){
            splashText = I18n.format("string.notch");
        }else if((date.getMonth() == Month.DECEMBER) && (date.getDayOfMonth() == 24 || date.getDayOfMonth() == 25)){
            splashText = I18n.format("string.christmas");
        }else if(date.getDayOfYear() == 1){
            splashText = I18n.format("string.newyear");
        }else if(date.getMonth() == Month.MARCH && date.getDayOfMonth() == 12){
            splashText = I18n.format("string.degubi.bday");
        }else if(date.getMonth() == Month.MARCH && date.getDayOfMonth() == 15) {
            splashText = "RIP";
        }else{
            java.util.List<String> splashes = readFromURL("http://pastebin.com/raw/97FAqQva");
            
            splashes.add(Minecraft.getMinecraft().getVersion() + "!");
            splashes.add(date.getYear() + "!");
            splashText = splashes.get(new Random().nextInt(splashes.size()));
        }
    }
}