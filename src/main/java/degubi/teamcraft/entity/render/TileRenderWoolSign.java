package degubi.teamcraft.entity.render;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class TileRenderWoolSign extends TileEntitySpecialRenderer<TileEntityWoolSign>{
    private static final ModelSign model = new ModelSign();
    private static final ResourceLocation res = new ResourceLocation("tcm:textures/blocks/woolsign.png");
    
    @Override
    public void render(TileEntityWoolSign woolTile, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
        GL11.glPushMatrix();
        float rotation = woolTile.getBlockMetadata();

        if (woolTile.getBlockType() == Main.WoolStandingSign){
            GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
            rotation *= 360 / 16.0F;
            GL11.glRotatef(-rotation, 0.0F, 1.0F, 0.0F);
            model.signStick.showModel = true;
        }else{
            int j = woolTile.getBlockMetadata();
            rotation = 0.0F;
            if(j == 2){
                rotation = 180.0F;
            }else if(j == 4){
                rotation = 90.0F;
            }else if(j == 5){
                rotation = -90.0F;
            }
            GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
            GL11.glRotatef(-rotation, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
            model.signStick.showModel = false;
        }
        bindTexture(res);
        GL11.glPushMatrix();
        GL11.glScalef(0.6666667F, -0.6666667F, -0.6666667F);
        model.renderSign();
        GL11.glPopMatrix();
        rotation = 0.016666668F * 0.6666667F;
        GL11.glTranslatef(0.0F, 0.5F * 0.6666667F, 0.07F * 0.6666667F);
        GL11.glScalef(rotation, -rotation, rotation);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * rotation);
        GL11.glDepthMask(false);
        
        if(destroyStage < 0){
            FontRenderer fontRender = getFontRenderer();
            for(int j = 0; j < woolTile.signText.length; ++j){
                if(woolTile.signText[j] != null){
                    List<ITextComponent> list = GuiUtilRenderComponents.splitText(woolTile.signText[j], 90, fontRender, false, true);
                    String s = !list.isEmpty() ? list.get(0).getFormattedText() : "";
                    if(j == woolTile.lineBeingEdited){
                        s = "> " + s + " <";
                        fontRender.drawString(s, -fontRender.getStringWidth(s) / 2, j * 10 - woolTile.signText.length * 5, 0);
                    }else{
                        fontRender.drawString(s, -fontRender.getStringWidth(s) / 2, j * 10 - woolTile.signText.length * 5, 0);
                    }
                }
            }
        }
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    /*@Override
    public void render(WoolSignTile woolTile, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
        Block block = woolTile.getBlockType();
        GL11.glPushMatrix();
        float f3;

        if (block == Main.WoolStandingSign)
        {
            GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
            float f2 = woolTile.getBlockMetadata() * 360 / 16.0F;
            GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
            model.signStick.showModel = true;
        }
        else
        {
            final int j = woolTile.getBlockMetadata();
            f3 = 0.0F;

            if (j == 2)
            {
                f3 = 180.0F;
            }

            if (j == 4)
            {
                f3 = 90.0F;
            }

            if (j == 5)
            {
                f3 = -90.0F;
            }

            GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * 0.6666667F, (float)z + 0.5F);
            GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
            model.signStick.showModel = false;
        }
        bindTexture(res);
        GL11.glPushMatrix();
        GL11.glScalef(0.6666667F, -0.6666667F, -0.6666667F);
        model.renderSign();
        GL11.glPopMatrix();
        
        f3 = 0.016666668F * 0.6666667F;
        GL11.glTranslatef(0.0F, 0.5F * 0.6666667F, 0.07F * 0.6666667F);
        GL11.glScalef(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GL11.glDepthMask(false);

        if (destroyStage < 0){
            for (int j = 0; j < woolTile.signText.length; ++j){
                if (woolTile.signText[j] != null){
                    final FontRenderer fontrenderer = getFontRenderer();
                    ITextComponent ichatcomponent = woolTile.signText[j];
                    List<ITextComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list != null && list.size() > 0 ? list.get(0).getFormattedText() : "";
                    if (j == woolTile.lineBeingEdited){
                        s = "> " + s + " <";
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - woolTile.signText.length * 5, 0);
                    }else{
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j * 10 - woolTile.signText.length * 5, 0);
                    }
                }
            }
        }
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    */
}