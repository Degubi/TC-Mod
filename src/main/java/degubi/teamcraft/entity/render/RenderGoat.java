package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderGoat extends RenderLiving<EntityGoat>{
    private static final ResourceLocation[] textures = new ResourceLocation[]{new ResourceLocation("tcm:textures/entity/goat1.png"),
             new ResourceLocation("tcm:textures/entity/goat3.png")};

    public RenderGoat(RenderManager ren) {
        super(ren, ModelGoat.INSTANCE, 0.5F);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityGoat entity) {
        if(entity.getGoatType() == 0){
            return textures[0];
        }else if(entity.getGoatType() == 1){
            return textures[1];
        }
        return ModelGoat.texture;
    }
}