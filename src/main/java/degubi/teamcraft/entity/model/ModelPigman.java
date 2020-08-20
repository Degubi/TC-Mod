package degubi.teamcraft.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class ModelPigman extends ModelBiped{
    public static final ModelPigman INSTANCE = new ModelPigman();
    public static final ResourceLocation text = new ResourceLocation("tcm:textures/entity/pigman.png");
    private ModelPigman(){
        bipedHead.setTextureOffset(27, 0).addBox(-2F, -4F, -5F, 4, 3, 1);
    }
    public void renderIce(){
        GL11.glPushMatrix();
        GL11.glScalef(0.99F, 0.99F, 0.99F);
        bipedHead.render(0.0625F);
        bipedBody.render(0.0625F);
        bipedRightArm.render(0.0625F);
        bipedLeftArm.render(0.0625F);
        bipedRightLeg.render(0.0625F);
        bipedLeftLeg.render(0.0625F);
        bipedHeadwear.render(0.0625F);
           GL11.glPopMatrix();
    }
}