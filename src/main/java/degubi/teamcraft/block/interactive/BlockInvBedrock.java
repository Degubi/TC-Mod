package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockInvBedrock extends Block {
    private final int type;

    public BlockInvBedrock(int bedrockType){
        super(Material.ROCK);
        type = bedrockType;

        Block modelBlock = Blocks.BEDROCK;
        setCreativeTab(Main.tabAdmin);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side){
        return type == 2 ? false : Minecraft.getMinecraft().player.capabilities.isCreativeMode;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag advanced) {
        if(type == 0){
            list.add("Works like Barrier, but ");
            list.add("in survival it has no black box");
        }else if(type == 2){
            list.add("Renders no Sides, useful for hiding things");
        }else{
            list.add("Replaces the old Piston Head Block");
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return type == 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos){
        return !Minecraft.getMinecraft().player.capabilities.isCreativeMode ? new AxisAlignedBB(0, 0, 0, 0, 0, 0) : FULL_BLOCK_AABB.offset(pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
        return type == 1 ? NULL_AABB : FULL_BLOCK_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager){
        return true;
    }
}