package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderPigman extends RenderBiped<EntityPigman> {

    public RenderPigman(RenderManager ren){
        super(ren, ModelPigman.INSTANCE, 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPigman entity) {
        return ModelPigman.text;
    }
}