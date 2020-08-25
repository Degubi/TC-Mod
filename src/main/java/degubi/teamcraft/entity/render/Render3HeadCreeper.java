package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class Render3HeadCreeper extends RenderLiving<EntityThreeHeadCreeper> {

    public Render3HeadCreeper(RenderManager man) {
        super(man, Model3HeadCreeper.INSTANCE, 0.5F);
    }

    @Override
    protected void preRenderCallback(EntityThreeHeadCreeper creeper, float partialTickTime){
        float f = creeper.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;

        if(f < 0.0F){
            f = 0.0F;
        }else if(f > 1.0F){
            f = 1.0F;
        }
        f = f * f * f;

        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GL11.glScalef(f2, f3, f2);
    }

    @Override
    protected int getColorMultiplier(EntityThreeHeadCreeper creeper, float lightBrightness, float partialTickTime){
        float f = creeper.getCreeperFlashIntensity(partialTickTime);

        if((int)(f * 10.0F) % 2 == 0){
            return 0;
        }
        return MathHelper.clamp((int)(f * 0.2F * 255.0F), 0, 255) << 24 | 822083583;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityThreeHeadCreeper entity){
        return Model3HeadCreeper.texture;
    }
}