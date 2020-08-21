package degubi.teamcraft.block;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;

public final class BlockBase extends Block {

    public BlockBase(Block modelBlock, CreativeTabs tab) {
        super(modelBlock.getDefaultState().getMaterial());
        
        setCreativeTab(tab);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }
}