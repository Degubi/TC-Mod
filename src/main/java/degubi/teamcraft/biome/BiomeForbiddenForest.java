package degubi.teamcraft.biome;

import java.util.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.*;

public final class BiomeForbiddenForest extends Biome{
    private static final WorldGenMegaJungle jungleTree = new WorldGenMegaJungle(false, 7, 15, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
    private static final WorldGenCanopyTree darkTree = new WorldGenCanopyTree(false);
    private static final WorldGenSavannaTree savannaTree = new WorldGenSavannaTree(false);
    private static final WorldGenTrees spruceTree = new WorldGenTrees(false, 12, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE), false);
    static final WorldGenTrees birchTree = new WorldGenTrees(false, 6, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH), false);

    public BiomeForbiddenForest() {
        super(new BiomeProperties("ForbiddenForest").setBaseHeight(0.1F).setHeightVariation(0.2F));
        decorator.bigMushroomsPerChunk = 8;
        decorator.treesPerChunk = 30;
        decorator.grassPerChunk = 20;
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableMonsterList.clear();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos){
        return 0x2f2f2f;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos){
        return 6975545;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float temp){
        return 0;
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand){
        int i = rand.nextInt(20);
        
        if(i > 18){
            return jungleTree;
        }else if(i > 13){
            return birchTree;
        }else if(i > 12){
            return spruceTree;
        }else if(i > 11){
            return savannaTree;
        }else if(i > 10){
            return darkTree;
        }
        return TREE_FEATURE;
    }
}