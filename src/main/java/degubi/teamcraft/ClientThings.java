package degubi.teamcraft;

import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.entity.*;
import degubi.teamcraft.entity.model.*;
import degubi.teamcraft.entity.render.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.monster.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.*;

@SideOnly(Side.CLIENT)
public final class ClientThings {
    public static final KeyBinding[] binds = new KeyBinding[5];
    public static KeyBinding zoomBind;
    private ClientThings() {}

    public static void registerClientThings(){
        ClientRegistry.registerKeyBinding(zoomBind = new KeyBinding("Zoom", Keyboard.KEY_C, "TeamCraft"));

        for(int k = 0; k < 5; ++k) {
            ClientRegistry.registerKeyBinding(binds[k] = new KeyBinding("CustomBind_" + (k + 1), Keyboard.KEY_NUMPAD1, "TeamCraft"));
        }

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIceBlock.class, new TileRenderIceBlock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoolSign.class, new TileRenderWoolSign());
        RenderingRegistry.registerEntityRenderingHandler(EntityGoat.class, RenderGoat::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPigman.class, RenderPigman::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCowBrine.class, RenderCowbrine::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityNetherCreeper.class, RenderNetherCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityThreeHeadCreeper.class, Render3HeadCreeper::new);

        IRenderFactory<EntitySpider> spiderRender = render -> new RenderSpiders(render, new ModelSpider());
        RenderingRegistry.registerEntityRenderingHandler(EntityPigSpider.class, render -> new RenderSpiders(render, ModelPigSpider.INSTANCE));
        RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonSpider.class, spiderRender);
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderSpider.class, spiderRender);
        RenderingRegistry.registerEntityRenderingHandler(EntityCureItem.class, render -> new RenderSnowball<>(render, Main.CureItem, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityClimberZombie.class, RenderLadderZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWolfMan.class, RenderWolfMan::new);
    }
}