package degubi.teamcraft.entity.model;

import degubi.teamcraft.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class ModelGoat extends ModelCow {
    private final ModelRenderer beard = new ModelRenderer(this, 0, 0).addBox(-1.0F, 3.0F, -5.0F, 2, 3, 1);
    private final ModelRenderer udders = new ModelRenderer(this, 14, 25).addBox(-2.0F, -3.0F, 0.0F, 2, 5, 2);
    private final ModelRenderer hornbrown = new ModelRenderer(this, 13, 16).addBox(-2.0F, -8.0F, -5.0F, 1, 7, 1).addBox(1.0F, -8.0F, -5.0F, 1, 7, 1);
    private final ModelRenderer hornyellow = new ModelRenderer(this, 18, 16).addBox(-2.0F, -8.0F, -5.0F, 1, 7, 1).addBox(1.0F, -8.0F, -5.0F, 1, 7, 1);
    private float headRotationAngleX;
    public static final ModelGoat INSTANCE = new ModelGoat();

    private ModelGoat(){
        beard.setRotationPoint(0.0F, 4.0F, -6.0F);
        head = new ModelRenderer(this, 0, 0);

        if(isChild) {
            head.addBox(-3.0F, -4.0F, -7.0F, 6, 6, 7);
        }else{
            head.addBox(-3.0F, -5.0F, -7.0F, 6, 6, 7);
        }

        head.setRotationPoint(0.0F, 4.0F, -6.0F);
        body = new ModelRenderer(this, 24, 5);
        body.addBox(-6.0F, -10.0F, -7.0F, 10, 16, 10);
        body.setRotationPoint(1.0F, 7.0F, 2.0F);
        leg1 = new ModelRenderer(this, 0, 19);
        leg1.addBox(-1.0F, 0.0F, -2.0F, 3, 10, 3);
        leg1.setRotationPoint(-3.0F, 14.0F, 7.0F);
        leg2 = new ModelRenderer(this, 0, 19);
        leg2.addBox(-1.0F, 0.0F, -2.0F, 3, 10, 3);
        leg2.setRotationPoint(2.0F, 14.0F, 7.0F);
        leg3 = new ModelRenderer(this, 0, 19);
        leg3.addBox(-2.0F, 0.0F, -2.0F, 3, 10, 3);
        leg3.setRotationPoint(-2.0F, 14.0F, -6.0F);
        leg4 = new ModelRenderer(this, 0, 19);
        leg4.addBox(-1.0F, 0.0F, -2.0F, 3, 10, 3);
        leg4.setRotationPoint(2.0F, 14.0F, -6.0F);
        hornbrown.setRotationPoint(0.0F, 4.0F, -6.0F);
        hornyellow.setRotationPoint(0.0F, 4.0F, -6.0F);
        udders.setRotationPoint(1.0F, 16.0F, 4.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale){
        EntityGoat goat = (EntityGoat) entity;

        if(goat.getCustomNameTag().equals("N0RRIS56")){
            GL11.glTranslatef(0, -3.1F, 0);
            GL11.glScalef(3F, 3F, 3F);
        }

        if(!isChild){
            udders.render(scale);
            beard.render(scale);
            if(goat.getHornColor() == 0){
                hornbrown.render(scale);
            }else{
                hornyellow.render(scale);
            }
        }
        super.render(goat, f, f1, f2, f3, f4, scale);
    }

    public void renderIce(){
        setRotationAngles(0, 0, 0, 0, 0, 0, null);
        GL11.glPushMatrix();
        GL11.glScalef(0.99f, 0.99f, 0.99f);
        head.render(0.0625F);
        body.render(0.0625F);
        leg1.render(0.0625F);
        leg2.render(0.0625F);
        leg3.render(0.0625F);
        leg4.render(0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
        head.rotationPointY = 6.0F + ((EntityGoat)entity).getHeadRotationPointY(partialTickTime) * 9.0F;
        headRotationAngleX = ((EntityGoat)entity).getHeadRotationAngleX(partialTickTime);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity){
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        hornbrown.rotateAngleX = head.rotateAngleX;
        hornbrown.rotateAngleY = head.rotateAngleY;
        hornbrown.rotateAngleX -= 0.2F;
        hornyellow.rotateAngleX = head.rotateAngleX;
        hornyellow.rotateAngleY = head.rotateAngleY;
        hornyellow.rotateAngleX -= 0.2F;
        beard.rotateAngleX = head.rotateAngleX;
        beard.rotateAngleY = head.rotateAngleY;
        head.rotateAngleX = headRotationAngleX;
    }
}