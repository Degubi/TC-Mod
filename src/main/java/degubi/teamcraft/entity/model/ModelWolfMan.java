package degubi.teamcraft.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class ModelWolfMan extends ModelBiped {
    private final ModelRenderer farok = new ModelRenderer(this, 60, 7).addBox(-0.5F, -20F, 2F, 1, 11, 1);

    public ModelWolfMan() {
        bipedLeftArm = new ModelRenderer(this, 32, 16);
        bipedLeftArm.addBox(-1F, 0F, 0F, 4, 12, 4);
        bipedRightArm = new ModelRenderer(this, 48, 16);
        bipedRightArm.addBox(-3F, 0F, 0F, 4, 12, 4);
        farok.rotateAngleX = -0.15707963267948966F;
        farok.rotateAngleZ = 3.141592653589793F;
        bipedRightLeg = new ModelRenderer(this, 16, 16);
        bipedRightLeg.addBox(-4F, 0F, 0F, 4, 12, 4);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.addBox(0F, 0F, 0F, 4, 12, 4);
        bipedBody = new ModelRenderer(this, 0, 0);
        bipedBody.addBox(-4F, 0F, 0F, 8, 12, 4);
        bipedBody.addChild(farok);
        bipedHead = new ModelRenderer(this, 24, 1);
        bipedHead.addBox(-4F, -7F, -1F, 8, 7, 7);
        bipedHead.addChild(new ModelRenderer(this, 48, 0).addBox(-2F, -4F, -5F, 4, 3, 4));
        bipedHead.addChild(new ModelRenderer(this, 12, 17).addBox(1F, -1F, -5F, 1, 1, 1));
        bipedHead.addChild(new ModelRenderer(this, 16, 17).addBox(-2F, -1F, -5F, 1, 1, 1));
        bipedHead.addChild(new ModelRenderer(this, 45, 16).addBox(-4F, -9F, 1F, 2, 2, 1));
        bipedHead.addChild(new ModelRenderer(this, 29, 16).addBox(2F, -9F, 1F, 2, 2, 1));
    }
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        bipedLeftArm.render(scale);
        bipedLeftLeg.render(scale);
        farok.render(scale);
        bipedHead.render(scale);
        bipedRightArm.render(scale);
        bipedRightLeg.render(scale);
        bipedBody.render(scale);
    }
}