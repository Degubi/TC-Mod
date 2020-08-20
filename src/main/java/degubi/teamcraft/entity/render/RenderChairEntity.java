package degubi.teamcraft.entity.render;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public final class RenderChairEntity extends RenderEntity{
    
    public RenderChairEntity(RenderManager ren) {
        super(ren);
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {}
}