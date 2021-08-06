package degubi.teamcraft.biome;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;

public final class BiomeHighBlocked extends Biome {

    public BiomeHighBlocked(String name, Block block, float baseHeight, float heightVariation) {
        super(new BiomeProperties(name).setTemperature(0F).setBaseHeight(baseHeight).setHeightVariation(heightVariation));

        topBlock = block.getDefaultState();
        fillerBlock = block.getDefaultState();
        spawnableCreatureList.clear();
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        super.decorate(world, rand, pos);

        if(topBlock.getBlock() == Blocks.PACKED_ICE){
            iceMobs(Main.IceCreeperBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IcePigmanBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IcePigSpiderBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IceSkeletonBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IceZombieBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IceThreeHeadCreeperBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            iceMobs(Main.IceGoatBlock.getDefaultState(), world, rand, pos.getX(), pos.getZ());
            icicleGeg(world, rand, pos.getX(), pos.getZ());
        }
    }

    private static void iceMobs(IBlockState toGen, World world, Random rand, int blockXPos, int blockZPos) {
        MutableBlockPos pos = new MutableBlockPos();

        for(int x = 0; x < 10; x++) {
            pos.setPos(blockXPos + rand.nextInt(16) + 8, 40 + rand.nextInt(200), blockZPos + rand.nextInt(16) + 8);

            if(world.getBlockState(pos).getBlock() == Blocks.PACKED_ICE) {
                world.setBlockState(pos, toGen.withProperty(BlockHorizontal.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 2);

                if(toGen.getBlock() != Main.IcePigSpiderBlock) {
                    world.setBlockState(pos.up(), Main.FakeBlock.getDefaultState(), 2);
                }
            }
        }
    }

    private static void icicleGeg(World world, Random random, int blockXPos, int blockZPos) {
        MutableBlockPos pos = new MutableBlockPos();

        for(int x = 0; x < 500; x++) {
            pos.setPos(blockXPos + random.nextInt(16) + 8, 25 + random.nextInt(200), blockZPos + random.nextInt(16) + 8);

            if(world.getBlockState(pos.up()).getBlock() == Blocks.PACKED_ICE && world.isAirBlock(pos)){
                world.setBlockState(pos, Main.IcIcle.getDefaultState(), 2);
            }
        }
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int x, int z, double noiseVal) {
        int i1 = x & 15;
        int j1 = z & 15;

        for(int k1 = 255; k1 >= 0; --k1) {
            if(k1 < 1 + rand.nextInt(5)) {
                chunkPrimer.setBlockState(j1, k1, i1, Blocks.ICE.getDefaultState());
            }

            if(k1 == 0) {
                chunkPrimer.setBlockState(j1, k1, i1, Blocks.BEDROCK.getDefaultState());
            }else{
                if(chunkPrimer.getBlockState(j1, k1, i1).getBlock() == Blocks.STONE) {
                    chunkPrimer.setBlockState(j1, k1, i1, fillerBlock);
                }
            }
        }
    }
}