package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderCowbrine extends RenderLiving<EntityCowBrine>{
    private static final ResourceLocation cowbrineTexture = new ResourceLocation("tcm:textures/entity/cowbrine.png");

    public RenderCowbrine(RenderManager ren) {
        super(ren, new ModelCow(), 0.5F);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityCowBrine entity) {
        return cowbrineTexture;
    }
}