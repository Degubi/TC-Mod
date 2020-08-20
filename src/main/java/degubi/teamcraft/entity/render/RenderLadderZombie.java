package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderLadderZombie extends RenderBiped<EntityClimberZombie>{
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ModelZombie zombieModel = new ModelZombie();

    public RenderLadderZombie(RenderManager renderManagerIn) {
        super(renderManagerIn, zombieModel, 0.5F);
        addLayer(new LayerBackpackZombie(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityClimberZombie entity) {
        return texture;
    }
}