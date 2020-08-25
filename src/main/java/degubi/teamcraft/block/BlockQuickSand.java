package degubi.teamcraft.block;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockQuickSand extends BlockFalling{

    public BlockQuickSand() {
        super(Material.SAND);
        setCreativeTab(Main.tabBlocks);
        setHardness(Blocks.SAND.getDefaultState().getBlockHardness(null, null) * 64);
        setResistance(Blocks.SAND.getExplosionResistance(null, null, null, null));
        setSoundType(Blocks.SAND.getSoundType(null, null, null, null));
        setHarvestLevel("shovel", 3);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
        return NULL_AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(world.getBlockState(pos.down()).getBlock() != Main.QuickSand && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
            world.setBlockState(pos.down(), Main.QuickSand.getDefaultState(), 2);
        }

        if(entity instanceof EntityFallingBlock){
            entity.setDead();
        }else{
            entity.motionX *= 0.0000001D;
            entity.motionY = 0.025D;
            entity.motionZ *= 0.0000001D;
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.SAND);
    }
}