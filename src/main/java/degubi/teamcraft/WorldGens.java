package degubi.teamcraft;

import com.google.common.base.*;
import degubi.teamcraft.worldgen.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.BlockLog.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.common.*;

public final class WorldGens implements IWorldGenerator {
    private static final IBlockState cocoa = Main.cocoaLeaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);
    private static final IBlockState log = Blocks.LOG.getDefaultState();
    private static final IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE);

    private static final WorldGenSponge sponge = new WorldGenSponge(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, Boolean.TRUE), Blocks.DIRT, Material.WATER);
    private static final WorldGenSponge basalt = new WorldGenSponge(Main.Basalt.getDefaultState(), Blocks.NETHERRACK, Material.LAVA);
    private static final WorldGenAppleTree tree = new WorldGenAppleTree();
    private static final WorldGenLakes quicksand = new WorldGenLakes(Main.QuickSand);
    private static final WorldGenTrees banana = new WorldGenTrees(true, 10, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), Main.bananaLeaves.getDefaultState(), true);

    private static final Predicate<IBlockState> isNether = k -> k.getBlock() == Blocks.NETHERRACK;
    private static final Predicate<IBlockState> isStone = k -> k.getBlock() == Blocks.STONE;
    private static final WorldGenMinable netherObsidian = new WorldGenMinable(Blocks.OBSIDIAN.getDefaultState(), 10, isNether);
    private static final WorldGenMinable netherCoal = new WorldGenMinable(Main.NetherCoalOre.getDefaultState(), 8, isNether);
    private static final WorldGenMinable netherIron = new WorldGenMinable(Main.NetherIronOre.getDefaultState(), 6, isNether);
    private static final WorldGenMinable netherGold = new WorldGenMinable(Main.NetherGoldOre.getDefaultState(), 5, isNether);
    private static final WorldGenMinable netherLapis = new WorldGenMinable(Main.NetherLapisOre.getDefaultState(), 12, isNether);
    private static final WorldGenMinable netherEmerald = new WorldGenMinable(Main.NetherEmeraldOre.getDefaultState(), 3, isNether);
    private static final WorldGenMinable netherDiamond = new WorldGenMinable(Main.NetherDiamondOre.getDefaultState(), 3, isNether);

    private static final WorldGenMinable marbleStone = new WorldGenMinable(Main.MarbleStone.getDefaultState(), 3, isStone);
    private static final WorldGenMinable gravelStone = new WorldGenMinable(Main.GravelStone.getDefaultState(), 5, isStone);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(world.provider.getDimension() == -1) {
            int x = chunkX * 16;
            int z = chunkZ * 16;

            addBaseGenNew(netherObsidian, world, random, x, z, 50, 20, 125);
            addBaseGenNew(netherCoal, world, random, x, z, 30, 20, 125);
            addBaseGenNew(netherIron, world, random, x, z, 25, 20, 125);
            addBaseGenNew(netherGold, world, random, x, z, 15, 20, 125);
            addBaseGenNew(netherLapis, world, random, x, z, 10, 20, 125);
            addBaseGenNew(netherEmerald, world, random, x, z, 10, 20, 125);
            addBaseGenNew(netherDiamond, world, random, x, z, 5, 20, 125);

            MutableBlockPos poss = new MutableBlockPos();
            for(int x1 = 0; x1 < 30; ++x1) {
                basalt.generate(world, random, poss.setPos(x + random.nextInt(16) + 8, 5 + random.nextInt(100), z + random.nextInt(16) + 8));
            }
        }else if(world.provider.getDimension() == 0) {
            int x = chunkX * 16;
            int z = chunkZ * 16;
            Biome b = world.getBiome(new BlockPos(x, 20, z));

            if(b.fillerBlock.getBlock() == Blocks.STONE){
                addBaseGenNew(marbleStone, world, random, x, z, 30, 40, 100);
                addBaseGenNew(gravelStone, world, random, x, z, 70, 20, 150);
            }
            if(b == Biomes.OCEAN){
                MutableBlockPos poss = new MutableBlockPos();

                for(int x1 = 0; x1 < 10; ++x1) {
                    sponge.generate(world, random, poss.setPos(x + random.nextInt(16) + 8, 40 + random.nextInt(60), z + random.nextInt(16) + 8));
                }
            }else if(b == Biomes.DESERT || b == Biomes.DESERT_HILLS || b == Biomes.MUTATED_DESERT){
                quicksand.generate(world, random, new BlockPos(x + random.nextInt(16) + 8, 40 + random.nextInt(60), z + random.nextInt(16) + 8));
            }else if(b == Biomes.FOREST){
                MutableBlockPos poss = new MutableBlockPos();

                for(int x1 = 0; x1 < 20; ++x1) {
                    tree.generate(world, random, poss.setPos(x + random.nextInt(16) + 8, 50 + random.nextInt(50), z + random.nextInt(16) + 8));
                }
            }else if(b == Biomes.SWAMPLAND){
                addOldTree(world, random, x, z);
            }else if(b == Biomes.JUNGLE) {
                MutableBlockPos poss = new MutableBlockPos();

                for(int x1 = 0; x1 < 10; x1++) {
                    banana.generate(world, random, poss.setPos(x + random.nextInt(16) + 8, 40 + random.nextInt(60), z + random.nextInt(16) + 8));
                    addCocoaGen(world, random, x, z);
                }
            }
        }
    }

    public static void addBaseGenNew(WorldGenMinable worldGen, World world, Random random, int blockXPos, int blockZPos, int chancesToSpawn, int minY, int maxY){
        int diffBtwnMinMaxY = maxY - minY;
        MutableBlockPos muPos = new MutableBlockPos();

        for(int x = 0; x < chancesToSpawn; ++x){
             worldGen.generate(world, random, muPos.setPos(blockXPos + random.nextInt(16), minY + random.nextInt(diffBtwnMinMaxY), blockZPos + random.nextInt(16)));
        }
    }

    public static void addCocoaGen(World world, Random random, int blockXPos, int blockZPos){
        for(int mul = 0; mul < 7; ++mul){
             BlockPos pos = new BlockPos(blockXPos + random.nextInt(16) + 8, 40 + random.nextInt(60), blockZPos + random.nextInt(16) + 8);

             if(world.getBlockState(pos.down()).getBlock() == Blocks.GRASS) {
                int x = -1;

                do {
                    x++;

                    for(int z = 0; z < 7; ++z) {
                        world.setBlockState(pos.south(3).west(3).north(x).east(z), cocoa, 2);
                    }
                    if(x < 5) {
                        for(int z = 0; z < 5; ++z) {
                            world.setBlockState(pos.up().south(2).west(2).north(x).east(z), cocoa, 2);
                            world.setBlockState(pos.up(3).south(2).west(2).north(x).east(z), cocoa, 2);
                        }
                    }
                    if(x < 3) {
                        for(int z = 0; z < 3; ++z) {
                            world.setBlockState(pos.up(2).south().west().north(x).east(z), cocoa, 2);
                            world.setBlockState(pos.up(4).south().west().north(x).east(z), cocoa, 2);
                        }
                    }
                }while(x < 6);

                for(x = 0; x < 5; ++x) {
                    world.setBlockState(pos.up(x), Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), 2);
                }
                world.setBlockState(pos.up(5), cocoa, 2);
            }
        }
    }

    public static void addOldTree(World world, Random rand, int blockXPos, int blockZPos){
        for(int mul = 0; mul < 2; ++mul){
            BlockPos pos = new BlockPos(blockXPos + rand.nextInt(16) + 8, 50 + rand.nextInt(30), blockZPos + rand.nextInt(16) + 8);

            if(world.getBlockState(pos.down()).getBlock() == Blocks.GRASS){
                int height = 9+rand.nextInt(4), x = -1;
                int nheight = height - 3;

                do {
                    x++;
                    for(int z = 0; z < 11; ++z) {
                        world.setBlockState(pos.south(5).west(5).up(height - 3).north(x).east(z), leaf, 2);
                    }
                    if(x < 9) {
                        for(int z = 0; z < 9; ++z) {
                            world.setBlockState(pos.south(4).west(4).up(height - 2).north(x).east(z), leaf, 2);
                        }
                    }
                    if(x < 7) {
                        for(int z = 0; z < 7; ++z) {
                            world.setBlockState(pos.south(3).west(3).up(height - 1).north(x).east(z), leaf, 2);
                        }
                    }
                    if(x < 5) {
                        for(int z = 0; z < 5; ++z) {
                            world.setBlockState(pos.south(2).west(2).up(height).north(x).east(z), leaf, 2);
                        }
                    }
                }while(x < 10);

                for(x = 0; x < nheight; ++x) {
                    world.setBlockState(pos.up(x), log, 2);
                    world.setBlockState(pos.north().up(x), log, 2);
                    world.setBlockState(pos.south().up(x), log, 2);
                    world.setBlockState(pos.east().up(x), log, 2);
                    world.setBlockState(pos.west().up(x), log, 2);

                    if(x < 2) {
                        world.setBlockState(pos.north().east().up(x), log, 2);
                        world.setBlockState(pos.north().west().up(x), log, 2);
                        world.setBlockState(pos.south().east().up(x), log, 2);
                        world.setBlockState(pos.south().west().up(x), log, 2);
                    }
                }

                world.setBlockState(pos.north(2).up(height - 4), log.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z), 2);
                world.setBlockState(pos.south(2).up(height - 4), log.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z), 2);
                world.setBlockState(pos.east(2).up(height - 4), log.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X), 2);
                world.setBlockState(pos.west(2).up(height - 4), log.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X), 2);
                world.setBlockState(pos.south(2), log.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z), 2);
                world.setBlockState(pos.north(2), log.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z), 2);
                world.setBlockState(pos.west(2), log.withProperty(BlockLog.LOG_AXIS, EnumAxis.X), 2);
                world.setBlockState(pos.east(2), log.withProperty(BlockLog.LOG_AXIS, EnumAxis.X), 2);
                world.setBlockState(pos.east().north().up(height - 4), log, 2);
                world.setBlockState(pos.east().south().up(height - 4), log, 2);
                world.setBlockState(pos.west().north().up(height - 4), log, 2);
                world.setBlockState(pos.west().south().up(height - 4), log, 2);
            }
        }
    }
}