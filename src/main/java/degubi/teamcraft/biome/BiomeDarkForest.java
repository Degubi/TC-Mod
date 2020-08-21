package degubi.teamcraft.biome;

import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.*;

public final class BiomeDarkForest extends Biome{
    private static final IBlockState log = Blocks.LOG.getDefaultState();
    private static final IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    public BiomeDarkForest() {
        super(new BiomeProperties("darkforest").setBaseHeight(0.1F).setHeightVariation(0.2F).setRainfall(1000F));
        topBlock = Blocks.GRASS.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();
        decorator.treesPerChunk = 3;
        decorator.bigMushroomsPerChunk = 1;
        decorator.grassPerChunk = 30;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos){
        return 6975545;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos){
        return 6975545;
    }
    
    @Override
    public int getWaterColorMultiplier() {
        return 0x004f0b;
    }
    
    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        super.decorate(world, rand, pos);
        
        MutableBlockPos pos1 = new MutableBlockPos();
        for(int x = 0; x < 30; x++){
            pos1.setPos(pos.getX() + rand.nextInt(16) + 8, 30 + rand.nextInt(70), pos.getZ() + rand.nextInt(16) + 8);
            
            if(world.getBlockState(pos1.up()).getBlock() == Blocks.LEAVES && world.getBlockState(pos1).getBlock() == Blocks.AIR){
                world.setBlockState(pos1, Blocks.WEB.getDefaultState(), 2);
            }
        }
        
        addBushGens(world, rand, pos.getX(), pos.getZ());
        addCustomTree1(world, rand, pos.getX(), pos.getZ());
    }
    
    private static void addBushGens(World world, Random rand, int blockXPos, int blockZPos){
        MutableBlockPos pos = new MutableBlockPos();
        
        for(int mul = 0; mul < 30; mul++){
             pos.setPos(blockXPos + rand.nextInt(16) + 8, 40 + rand.nextInt(80), blockZPos + rand.nextInt(16) + 8);
             
             if(world.getBlockState(pos.down()).getBlock() == Blocks.GRASS && world.isAirBlock(pos.up(2).west(3)) && world.isAirBlock(pos.up(2).east(3)) && world.isAirBlock(pos.up(2).north(3)) && world.isAirBlock(pos.up(2).south(3))){
                int height = 1+rand.nextInt(2);
                if(rand.nextBoolean()){
                    world.setBlockState(pos.up(height), leaf, 2);
                    world.setBlockState(pos.north().up(height - 1), leaf, 2);
                    world.setBlockState(pos.south().up(height - 1), leaf, 2);
                    world.setBlockState(pos.east().up(height - 1), leaf, 2);
                    world.setBlockState(pos.west().up(height - 1), leaf, 2);
                }else{
                    int x = -1;
                    if(height == 2) {
                        do {
                            x++;
                            for(int z = 0; z < 5; ++z) {
                                world.setBlockState(pos.east(-2 + x).north(-2 + z), leaf, 2);
                            }
                            if(x < 3) {
                                for(int z = 0; z < 3; ++z) {
                                    world.setBlockState(pos.east(-1 + x).north(-1 + z).up(), leaf, 2);
                                }
                            }
                        }while(x < 4);
                        
                        world.setBlockState(pos.up(2), leaf, 2);
                        world.setBlockState(pos.up(2).north(), leaf, 2);
                        world.setBlockState(pos.up(2).south(), leaf, 2);
                        world.setBlockState(pos.up(2).east(), leaf, 2);
                        world.setBlockState(pos.up(2).west(), leaf, 2);
                    }else {
                        do {
                            x++;
                            for(int z = 0; z < 3; ++z) {
                                world.setBlockState(pos.east(-1 + x).north(-1 + z), leaf, 2);
                            }
                        }while(x < 2);
                        world.setBlockState(pos.up(), leaf, 2);
                    }
                }
                
                for(int k = 0; k < height; ++k) {
                    world.setBlockState(pos.up(k), log, 2);
                }
            }
        }
    }
    
    private static void addCustomTree1(World world, Random rand, int blockXPos, int blockZPos){
        MutableBlockPos pos = new MutableBlockPos();

        for(int mul = 0; mul < 15; mul++){
            pos.setPos(blockXPos + rand.nextInt(16) + 8, 40 + rand.nextInt(60), blockZPos + rand.nextInt(16) + 8);
            
            if(world.getBlockState(pos.down()).getBlock() == Blocks.GRASS){
                int rr = 8+rand.nextInt(8);
                
                for(int lal = 0; lal <= rr; ++ lal)
                world.setBlockState(pos.up(lal), log, 2);
                
                world.setBlockState(pos.north().up(rr), leaf, 2);
                world.setBlockState(pos.south().up(rr), leaf, 2);
                world.setBlockState(pos.east().up(rr), leaf, 2);
                world.setBlockState(pos.west().up(rr), leaf, 2);
                world.setBlockState(pos.up(rr), leaf, 2);
                
                int asd = rr - 6;
                for(int lal = 0; lal < asd; lal += 2){
                    world.setBlockState(pos.north().up(lal + 5), leaf, 2);
                    world.setBlockState(pos.south().up(lal + 5), leaf, 2);
                    world.setBlockState(pos.east().up(lal + 5), leaf, 2);
                    world.setBlockState(pos.west().up(lal + 5), leaf, 2);
                    
                    for(int x = 0; x < 3; ++ x){
                        for(int z = 0; z < 3; ++z){
                            world.setBlockState(pos.south().west().up(lal + 6).north(x).east(z), leaf, 2);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand){
        return rand.nextInt(2) == 0 ? BiomeForbiddenForest.birchTree : TREE_FEATURE;
    }
}