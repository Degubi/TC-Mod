package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockPressurePlates extends BlockPressurePlate {
    private final Sensitivity sensitivitye;
    private final boolean isGlass, isObsi;

    public BlockPressurePlates(Block infBlock) {
        super(infBlock.getDefaultState().getMaterial(), Sensitivity.EVERYTHING);

        sensitivitye = infBlock == Blocks.PLANKS ? Sensitivity.EVERYTHING : Sensitivity.MOBS;
        isGlass = infBlock == Blocks.GLASS;
        isObsi = infBlock == Blocks.OBSIDIAN;
        setCreativeTab(Main.tabRedstone);
        setHardness(infBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(infBlock.getExplosionResistance(null, null, null, null));
        setSoundType(infBlock.getSoundType(null, null, null, null));
        setHarvestLevel(infBlock.getHarvestTool(infBlock.getDefaultState()), infBlock.getHarvestLevel(infBlock.getDefaultState()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return isGlass ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos pos)    {
        AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
        List<? extends Entity> list;

        if(isObsi) {
            list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        }else if(sensitivitye == Sensitivity.EVERYTHING) {
            list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
        }else if(sensitivitye == Sensitivity.MOBS) {
            list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        }else {
            list = new ArrayList<>();
        }

        for(Entity entity : list){
            if (!entity.doesEntityNotTriggerPressurePlate()){
                return 15;
            }
        }
        return 0;
    }
}