package degubi.teamcraft.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class Model3HeadCreeper extends ModelCreeper{
    private final ModelRenderer head3 = new ModelRenderer(this, 0, 0).addBox(-4F, -10F, -4F, 8, 8, 8);
    private final ModelRenderer head2 = new ModelRenderer(this, 0, 0).addBox(-4F, -10F, -4F, 8, 8, 8);
    public static final Model3HeadCreeper INSTANCE = new Model3HeadCreeper();
    public static final ResourceLocation texture = new ResourceLocation("tcm:textures/entity/3headcreeper.png");
    
    private Model3HeadCreeper(){
        head3.setRotationPoint(-8F, 6F, 0F);
        head2.setRotationPoint(8F, 6F, 0F);
    }
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale){
        super.render(entity, f, f1, f2, f3, f4, scale);
        head3.render(scale);
        head2.render(scale);
    }
    @Override
    public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, Entity paramEntity){
        head2.rotateAngleY = paramFloat4 / (180F / (float)Math.PI);
        head2.rotateAngleX = paramFloat5 / (180F / (float)Math.PI);
        head3.rotateAngleY = paramFloat4 / (180F / (float)Math.PI);
        head3.rotateAngleX = paramFloat5 / (180F / (float)Math.PI);
        super.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
    }
}