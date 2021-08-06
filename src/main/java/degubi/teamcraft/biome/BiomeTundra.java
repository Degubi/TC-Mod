package degubi.teamcraft.biome;

import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.feature.*;

public final class BiomeTundra extends Biome {
    private static final WorldGenTrees spruceTree = new WorldGenTrees(false, 12, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE), false);

    public BiomeTundra() {
        super(new BiomeProperties("Tundra").setBaseHeight(0.2F).setHeightVariation(0.5F).setSnowEnabled());
        topBlock = Blocks.GRASS.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();
        decorator.treesPerChunk = 5;
        spawnableCreatureList.clear();
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x74614c;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return spruceTree;
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
        IBlockState iblockstate = topBlock;
        int k = -1;
        int l = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;

        for (int k1 = 255; k1 >= 0; --k1){
            if (k1 <= rand.nextInt(5)){
                primer.setBlockState(j1, k1, i1, Blocks.BEDROCK.getDefaultState());
            }else{
                IBlockState iblockstate2 = primer.getBlockState(j1, k1, i1);

                if (iblockstate2.getMaterial() == Material.AIR){
                    k = -1;
                }else if (iblockstate2.getBlock() == Blocks.STONE){
                    if (k == -1){
                        if (l <= 0){
                            iblockstate = null;
                        }else if (k1 >= 59 && k1 <= 64){
                            iblockstate = topBlock;
                        }

                        if(k1 < 63 && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)){
                            if (getTemperature(new BlockPos(x, k1, z)) < 0.15F){
                                iblockstate = Blocks.ICE.getDefaultState();
                            }else{
                                iblockstate = Blocks.WATER.getDefaultState();
                            }
                        }

                        k = l;
                        if(k1 >= 70){
                            primer.setBlockState(j1, k1, i1, Blocks.SNOW.getDefaultState());
                        }else if (k1 >= 62){
                            primer.setBlockState(j1, k1, i1, iblockstate);

                            if(rand.nextInt(15) == 2 && primer.getBlockState(j1, k1, i1).getBlock() != Blocks.AIR){
                                primer.setBlockState(j1, k1 + 1, i1, Blocks.SNOW_LAYER.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x74614c;
    }
}