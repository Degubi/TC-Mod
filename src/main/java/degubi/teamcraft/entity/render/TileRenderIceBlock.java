package degubi.teamcraft.entity.render;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.entity.model.*;
import net.minecraft.block.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.*;

@SideOnly(Side.CLIENT)
public final class TileRenderIceBlock extends TileEntitySpecialRenderer<TileEntityIceBlock>{
    private static final ModelCreeper creeperModel = new ModelCreeper();
    private static final ModelZombie zombieModel = new ModelZombie();
    private static final ModelSkeleton skeletonModel = new ModelSkeleton();

    private static final ResourceLocation[] textures = new ResourceLocation[] { new ResourceLocation("textures/entity/creeper/creeper.png"),
                                                                                new ResourceLocation("textures/entity/skeleton/skeleton.png"),
                                                                                new ResourceLocation("textures/entity/zombie/zombie.png") };
    private static EntitySkeleton skeleton;
    private static EntityZombie zombie;

    public TileRenderIceBlock() {
        skeletonModel.isChild = false;
        skeletonModel.isSneak = true;
        zombieModel.isChild = false;
    }

    private void loadLazy() {
        if(skeleton == null) {
            skeleton = new EntitySkeleton(getWorld());
        }if(zombie == null) {
            zombie = new EntityZombie(getWorld());
        }
    }

    @Override
    public void render(TileEntityIceBlock tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Block block = tile.getBlockType();
        EnumFacing direction = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockHorizontal.FACING);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GL11.glRotatef(180, 0F, 0F, 1F);

        if(direction == EnumFacing.NORTH){
            GL11.glRotatef(180, 0, 1, 0);
        }else if(direction == EnumFacing.WEST){
            GL11.glRotatef(90, 0, 1, 0);
        }else if(direction == EnumFacing.EAST){
            GL11.glRotatef(-90F, 0, 1, 0);
        }

        if(block == Main.IceCreeperBlock){
            bindTexture(textures[0]);
            creeperModel.render(null, 0, 0, 0, 0, 0, 0.0625F);
        }else if(block == Main.IcePigmanBlock){
            bindTexture(ModelPigman.text);
            ModelPigman.INSTANCE.renderIce();
        }else if(block == Main.IcePigSpiderBlock){
            bindTexture(ModelPigSpider.texture);
            ModelPigSpider.INSTANCE.renderIce();
        }else if(block == Main.IceSkeletonBlock){
            loadLazy();
            bindTexture(textures[1]);
            skeletonModel.render(skeleton, 0, 0, 0, 0, 0, 0.0625F);
        }else if(block == Main.IceZombieBlock){
            loadLazy();
            bindTexture(textures[2]);
            GL11.glPushMatrix();
            GL11.glScalef(0.95F, 0.95F, 0.95F);
            zombieModel.render(zombie, 0, 0, 0, 0, 0, 0.0625F);
            GL11.glPopMatrix();
        }else if(block == Main.IceThreeHeadCreeperBlock){
            bindTexture(Model3HeadCreeper.texture);
            Model3HeadCreeper.INSTANCE.render(null, 0, 0, 0, 0, 0, 0.0625F);
        }else{
            bindTexture(RenderGoat.textures[2]);
            ModelGoat.INSTANCE.renderIce();
        }
        GL11.glPopMatrix();
    }
}