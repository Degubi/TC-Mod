package degubi.teamcraft.block;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockOres extends Block {
    
    private final Block modelBlock;
    
    public BlockOres(Block modelBlock) {
        super(modelBlock.getDefaultState().getMaterial());
        
        this.modelBlock = modelBlock;
        setCreativeTab(Main.tabBlocks);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        if(modelBlock == Blocks.COAL_ORE) {
            return Main.NetherCoal;
        }else if(modelBlock == Blocks.DIAMOND_ORE) {
            return Items.DIAMOND;
        }else if(modelBlock == Blocks.LAPIS_ORE) {
            return Items.DYE;
        }else if(modelBlock == Blocks.STONE) {
            return Item.getItemFromBlock(Main.MarbleCobble);
        }else if(modelBlock == Blocks.EMERALD_ORE) {
            return Items.EMERALD;
        }
        return Item.getItemFromBlock(this);
    }
    
    @Override
    public int damageDropped(IBlockState state){
        return modelBlock == Blocks.LAPIS_ORE ? 4 : 0;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune){
        if(modelBlock == Blocks.COAL_ORE){
            return 1 + RANDOM.nextInt(2);
        }else if(modelBlock == Blocks.LAPIS_ORE || modelBlock == Blocks.EMERALD_ORE){
            return 3 + RANDOM.nextInt(4);
        }else if(modelBlock == Blocks.DIAMOND_ORE){
            return 5 + RANDOM.nextInt(3);
        }else{
            return 0;
        }
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random rand){
        return modelBlock == Blocks.LAPIS_ORE ? 3 + rand.nextInt(4) : 1;
    }
}