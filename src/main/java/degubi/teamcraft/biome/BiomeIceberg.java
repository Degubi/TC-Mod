package degubi.teamcraft.biome;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;

public final class BiomeIceberg extends Biome{
    
    public BiomeIceberg(){
        super(new BiomeProperties("iceberg").setBaseHeight(-0.25F).setHeightVariation(0.25F).setSnowEnabled().setTemperature(0F));
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
        }
    }
    
    private static void iceMobs(IBlockState toGen, World world, Random rand, int blockXPos, int blockZPos){
        for(int x = 0; x < 10; x++){
            BlockPos pos = new BlockPos(blockXPos + rand.nextInt(16) + 8, 40 + rand.nextInt(200), blockZPos + rand.nextInt(16) + 8);
            
            if(world.getBlockState(pos).getBlock() == Blocks.PACKED_ICE){
                world.setBlockState(pos, toGen.withProperty(BlockHorizontal.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 2);
                
                if(toGen.getBlock() != Main.IcePigSpiderBlock){
                    world.setBlockState(pos.up(), Main.FakeBlock.getDefaultState(), 2);
                }
            }
        }
    }
    
    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunkPrimer, int x, int z, double noiseVal){
        int i1 = x & 15;
        int j1 = z & 15;
        
        for(int k1 = 255; k1 >= 0; --k1){
            if(k1 < 1+rand.nextInt(5)){
                chunkPrimer.setBlockState(j1, k1, i1, Blocks.PACKED_ICE.getDefaultState());
            }
            if(k1 == 0){
                chunkPrimer.setBlockState(j1, k1, i1, Blocks.BEDROCK.getDefaultState());
            }else{
                IBlockState iblockstate2 = chunkPrimer.getBlockState(j1, k1, i1);
                
                if(iblockstate2.getBlock() == Blocks.STONE){
                    chunkPrimer.setBlockState(j1, k1, i1, Blocks.PACKED_ICE.getDefaultState());
                }
                if(rand.nextInt(5) == 3 && chunkPrimer.getBlockState(j1, k1, i1).getBlock() == Blocks.PACKED_ICE && chunkPrimer.getBlockState(j1, k1 + 1, i1).getBlock() == Blocks.AIR){
                    chunkPrimer.setBlockState(j1, k1 + 1, i1, Blocks.SNOW_LAYER.getDefaultState());
                }
            }
        }
    }
}