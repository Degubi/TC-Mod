package degubi.teamcraft.block;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockFakeBlock extends Block {

    public BlockFakeBlock(){
        super(Material.ICE);

        Block modelBlock = Blocks.ICE;
        setDefaultSlipperiness(0.98F);
        blockParticleGravity = 0;
        setCreativeTab(Main.tabAdmin);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state){
        world.setBlockToAir(pos.down());
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess acc, BlockPos pos, EnumFacing side){
        IBlockState blocks = acc.getBlockState(pos.offset(side));
        return !blocks.isOpaqueCube() && side != EnumFacing.DOWN;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemById(0);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Block.getBlockById(0));
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        return false;
    }
}