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

public final class BlockDoors extends BlockDoor {
    private final boolean isAlpha;

    public BlockDoors(Block infBlock) {
        super(infBlock.getDefaultState().getMaterial());
        isAlpha = infBlock == Blocks.ICE;

        setCreativeTab(Main.tabRedstone);
        setHardness(infBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(infBlock.getExplosionResistance(null, null, null, null));
        setSoundType(infBlock.getSoundType(null, null, null, null));
        setHarvestLevel(infBlock.getHarvestTool(infBlock.getDefaultState()), infBlock.getHarvestLevel(infBlock.getDefaultState()));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return state.getValue(BlockDoor.HALF) == EnumDoorHalf.UPPER ? null : Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state){
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return isAlpha ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT_MIPPED;
    }
}