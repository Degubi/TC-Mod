package degubi.teamcraft.block;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockHalfBlockMaker extends Block {
    
    public BlockHalfBlockMaker() {
        super(Material.ROCK);
        
        Block modelBlock = Blocks.STONE;
        setCreativeTab(Main.tabRedstone);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack item = player.getHeldItem(hand);
        player.addItemStackToInventory(new ItemStack(handleBlocks(item), 2));
        item.shrink(1);
        return true;
    }
    
    private static Block handleBlocks(ItemStack xItem) {
        Block base = Block.getBlockFromItem(xItem.getItem());
        Block returnBlock = BlockHalfBlocks.returnBlock.get(base);
        int meta = xItem.getMetadata();
        
        if(base == Blocks.STONE) {
            if(meta == 0) {
                returnBlock = Main.HalfStone;
            }else if(meta == 2) {
                returnBlock = Main.HalfGranite;
            }else if(meta == 4) {
                returnBlock = Main.HalfDiorite;
            }else if(meta == 6) {
                returnBlock = Main.HalfAndesite;
            }
        }else if(base == Blocks.PLANKS) {
            switch(meta) {
            case 0: returnBlock = Main.HalfOakPlanksBlock; break;
            case 1: returnBlock = Main.HalfSprucePlanksBlock; break;
            case 2: returnBlock = Main.HalfBirchPlanksBlock; break;
            case 3: returnBlock = Main.HalfJunglePlanksBlock; break;
            case 4: returnBlock = Main.HalfAcaciaPlanksBlock; break;
            default: returnBlock = Main.HalfDarkOakPlanksBlock; break;
            }
        }else if(base == Blocks.LOG) {
            if(meta == 0) {
                returnBlock = Main.HalfOakLog;
            }else if(meta == 1) {
                returnBlock = Main.HalfSpruceLog;
            }else if(meta == 2) {
                returnBlock = Main.HalfBirchLog;
            }else{
                returnBlock = Main.HalfJungleLog;
            }
        }else if(base == Blocks.LOG2) {
            if(meta == 0) {
                returnBlock = Main.HalfAcaciaLog;
            }else{
                returnBlock = Main.HalfDarkOakLog;
            }
        }else if(base == Blocks.STAINED_GLASS) {
            switch(meta) {
            case 0: returnBlock = Main.HalfWhiteGlass; break;
            case 1: returnBlock = Main.HalfOrangeGlass; break;
            case 2: returnBlock = Main.HalfMagentaGlass; break;
            case 3: returnBlock = Main.HalfLightBlueGlass; break;
            case 4: returnBlock = Main.HalfYellowGlass; break;
            case 5: returnBlock = Main.HalfLimeGlass; break;
            case 6: returnBlock = Main.HalfPinkGlass; break;
            case 7: returnBlock = Main.HalfGrayGlass; break;
            case 8: returnBlock = Main.HalfSilverGlass; break;
            case 9: returnBlock = Main.HalfCyanGlass; break;
            case 10: returnBlock = Main.HalfPurpleGlass; break;
            case 11: returnBlock = Main.HalfBlueGlass; break;
            case 12: returnBlock = Main.HalfBrownGlass; break;
            case 13: returnBlock = Main.HalfGreenGlass; break;
            case 14: returnBlock = Main.HalfRedGlass; break;
            default: returnBlock = Main.HalfBlackGlass; break;
            }
        }else if(base == Blocks.SANDSTONE) {
            if(meta == 0) {
                returnBlock = Main.HalfSandstone;
            }else if(meta == 2) {
                returnBlock = Main.HalfSmoothSandstone;
            }
        }else if(base == Blocks.QUARTZ_BLOCK) {
            if(meta == 1) {
                returnBlock = Main.HalfChiseledQuartz;
            }else if(meta == 2) {
                returnBlock = Main.HalfQuartzPillar;
            }else {
                returnBlock = Main.HalfQuartz;
            }
        /*}else if(base == Main.StoneVariants) {
            if(meta == 2) {
                returnBlock = Main.HalfBasaltBrick;
            }else if(meta == 6) {
                returnBlock = Main.HalfGraniteStoneBrick;
            }else if(meta == 7) {
                returnBlock = Main.HalfAndesiteStoneBrick;
            }*/
        }
        return returnBlock;
    }
}