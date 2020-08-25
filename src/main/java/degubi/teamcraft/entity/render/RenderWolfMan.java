package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderWolfMan extends RenderLiving<EntityWolfMan> {
    private static final ResourceLocation texture = new ResourceLocation("tcm:textures/entity/wolfman.png");

    public RenderWolfMan(RenderManager rendermanagerIn){
        super(rendermanagerIn, new ModelWolfMan(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWolfMan entity) {
        return texture;
    }
}