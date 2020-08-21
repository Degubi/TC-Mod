package degubi.teamcraft.entity.model;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.render.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

public final class LayerBackpackZombie implements LayerRenderer<EntityClimberZombie> {
    private static final ModelLadderZombie ladderZombie = new ModelLadderZombie();
    private static final ResourceLocation backpackTexture = new ResourceLocation("tcm:textures/entity/backpack.png");
    private final RenderLadderZombie render;
    
    public LayerBackpackZombie(RenderLadderZombie theRender) {
        render = theRender;
    }
    
    @Override
    public void doRenderLayer(EntityClimberZombie zombie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        render.bindTexture(backpackTexture);
        ladderZombie.render(zombie, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    private static final class ModelLadderZombie extends ModelBase {
        private final ModelRenderer bag = new ModelRenderer(this, 0, 0).addBox(0F, 0F, 0F, 8, 10, 4);
        
        public ModelLadderZombie(){
            bag.setRotationPoint(-4F, 0F, 2F);
        }
        
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale){
            if(((EntityClimberZombie)entity).isChild()) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glTranslatef(0F, 1.5F, 0F);
                bag.render(scale);
            }else{
                bag.render(scale);
            }
        }
    }
}