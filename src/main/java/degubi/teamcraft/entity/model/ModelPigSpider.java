package degubi.teamcraft.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class ModelPigSpider extends ModelSpider {
    public static final ModelPigSpider INSTANCE = new ModelPigSpider();
    public static final ResourceLocation texture = new ResourceLocation("tcm:textures/entity/pig_spider_day.png");

    private ModelPigSpider(){
        spiderHead.setTextureOffset(0, 0).addBox(-2F, 0F, -9F, 4, 3, 1);
        spiderNeck = new ModelRenderer(this, 0, 12);
        spiderNeck.addBox(-5F, -4F, -6F, 10, 8, 12);
        spiderNeck.setRotationPoint(0F, 16F, 3F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale){
        if(entity.getCustomNameTag().contains("Degubi")){
            GL11.glScalef(0.3F, 0.3F, 0.3F);
            GL11.glTranslatef(0, 3F, 0);
        }
        if(entity.world.provider.getDimension() == -1){
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            GL11.glTranslatef(0, -0.45F, 0);
        }

        setRotationAngles(f, f1, f2, f3, f4, scale, entity);
        spiderHead.render(scale);
        spiderLeg1.render(scale);
        spiderLeg2.render(scale);
        spiderLeg3.render(scale);
        spiderLeg4.render(scale);
        spiderLeg5.render(scale);
        spiderLeg6.render(scale);
        spiderLeg7.render(scale);
        spiderLeg8.render(scale);
        spiderNeck.render(scale);
    }

    public void renderIce(){
        setRotationAngles(0, 0, 0, 0, 0, 0.0625F, null);
        spiderHead.render(0.0625F);
        spiderLeg1.render(0.0625F);
        spiderLeg2.render(0.0625F);
        spiderLeg3.render(0.0625F);
        spiderLeg4.render(0.0625F);
        spiderLeg5.render(0.0625F);
        spiderLeg6.render(0.0625F);
        spiderLeg7.render(0.0625F);
        spiderLeg8.render(0.0625F);
        spiderNeck.render(0.0625F);
    }
}