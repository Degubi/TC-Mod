package degubi.teamcraft.block;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockBase extends Block {
    private final Block shitBlock;

    public BlockBase(Block modelBlock, CreativeTabs tab) {
        super(modelBlock.getDefaultState().getMaterial());
        shitBlock = modelBlock;
        setCreativeTab(tab);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return shitBlock.getBlockLayer();
    }
}