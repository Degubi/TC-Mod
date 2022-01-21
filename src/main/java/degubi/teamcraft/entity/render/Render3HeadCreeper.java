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
    protected void preRenderCallback(EntityThreeHeadCreeper creeper, float partialTickTime) {
        float intensity = getCreeperFlashIntensity(partialTickTime, creeper);

        if(intensity < 0.0F) {
            intensity = 0.0F;
        }else if(intensity > 1.0F) {
            intensity = 1.0F;
        }

        float f1 = 1.0F + MathHelper.sin(intensity * 100.0F) * intensity * 0.01F;

        intensity = intensity * intensity * intensity;

        float f2 = (1.0F + intensity * 0.4F) * f1;
        float f3 = (1.0F + intensity * 0.1F) / f1;
        GL11.glScalef(f2, f3, f2);
    }

    @Override
    protected int getColorMultiplier(EntityThreeHeadCreeper creeper, float lightBrightness, float partialTickTime) {
        float intensity = getCreeperFlashIntensity(partialTickTime, creeper);

        return (int)(intensity * 10.0F) % 2 == 0 ? 0
                                                 : MathHelper.clamp((int)(intensity * 0.2F * 255.0F), 0, 255) << 24 | 822083583;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityThreeHeadCreeper entity) {
        return Model3HeadCreeper.texture;
    }

    private float getCreeperFlashIntensity(float partialTickTime, EntityThreeHeadCreeper creeper) {
        return (creeper.lastActiveTime + (creeper.timeSinceIgnited - creeper.lastActiveTime) * partialTickTime) / (creeper.fuseTime - 2);
    }
}