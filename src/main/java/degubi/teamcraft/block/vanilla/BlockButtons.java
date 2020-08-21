package degubi.teamcraft.block.vanilla;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockButtons extends BlockButtonWood {
    private final boolean isIce, isRedstone;
    
    public BlockButtons(Block infBlock) {
        setCreativeTab(Main.tabRedstone);
        setHardness(infBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(infBlock.getExplosionResistance(null, null, null, null));
        setSoundType(infBlock.getSoundType(null, null, null, null));
        setHarvestLevel(infBlock.getHarvestTool(infBlock.getDefaultState()), infBlock.getHarvestLevel(infBlock.getDefaultState()));
        isIce = infBlock == Blocks.ICE;
        isRedstone = infBlock == Blocks.REDSTONE_BLOCK;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return isIce ? BlockRenderLayer.TRANSLUCENT : super.getBlockLayer();
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        if(!world.isRemote && world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - state.getLightOpacity(world, pos) && isIce){
            if (!world.provider.isSurfaceWorld()){
                world.setBlockToAir(pos);
            }
            
            dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
        }
        super.updateTick(world, pos, state, rand);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return isIce ? Item.getItemById(0) : super.getItemDropped(state, rand, fortune);
    }
    
    @Override
    public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side){
        return isRedstone && !state.getValue(BlockButton.POWERED).booleanValue() ? 15 : 0;
    }
}