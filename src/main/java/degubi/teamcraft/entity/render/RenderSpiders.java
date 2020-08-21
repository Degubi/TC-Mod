package degubi.teamcraft.entity.render;

import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT) 
public final class RenderSpiders extends RenderLiving<EntityLiving> {
    private static final ResourceLocation night = new ResourceLocation("tcm:textures/entity/pig_spider_night.png");
    private static final ResourceLocation nether = new ResourceLocation("tcm:textures/entity/pig_spider_nether.png");
    private static final ResourceLocation ender = new ResourceLocation("tcm:textures/entity/spider_ender.png");
    private static final ResourceLocation skele = new ResourceLocation("tcm:textures/entity/spider_skele.png");

    public RenderSpiders(RenderManager man, ModelBase model){
        super(man, model, 0.5F);
    }
    
    @Override
    public ResourceLocation getEntityTexture(EntityLiving entity){
        if(entity instanceof EntityPigSpider){
            if(((EntityPigSpider) entity).isNetherSpider()){
                return nether;
            }
            if(entity.world.getSunBrightness(1) < 0.5F || entity.isBurning()){
                return night;
            }
            return ModelPigSpider.texture;
        }
        if(entity instanceof EntityEnderSpider){
            return ender;
        }
        return skele;
    }
}