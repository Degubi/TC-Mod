package degubi.teamcraft.biome;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.BlockLog.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;

public final class BiomeIceDesert extends Biome {
    private static final IBlockState cactus = Main.Cactus.getDefaultState();
    private static final WorldGenMinable ice = new WorldGenMinable(Blocks.ICE.getDefaultState(), 10, k -> k == Blocks.SAND);
    private static final WorldGenMinable packedIce = new WorldGenMinable(Blocks.PACKED_ICE.getDefaultState(), 6, k -> k == Blocks.SANDSTONE);

    public BiomeIceDesert() {
        super(new BiomeProperties("IceDesert").setBaseHeight(0.3F).setHeightVariation(0.1F).setTemperature(0).setRainfall(0.1F));

        fillerBlock = Blocks.SAND.getDefaultState();
        topBlock = Blocks.SNOW_LAYER.getDefaultState();
    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        super.decorate(world, rand, pos);

        for(int x = 0; x < 20; x++){
            BlockPos pos1 = new BlockPos(pos.getX() + rand.nextInt(16) + 8, 30 + rand.nextInt(60), pos.getZ() + rand.nextInt(16) + 8);

            if(world.getBlockState(pos1.down()).getBlock() == Blocks.SAND && world.getBlockState(pos1).getBlock() == Blocks.SNOW_LAYER){
                for(int up = 0; up < 7; ++up) {
                    world.setBlockState(pos1.up(up), cactus, 2);
                }

                if(rand.nextBoolean()){
                    world.setBlockState(pos1.up(3).north(), cactus.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z), 2);
                    world.setBlockState(pos1.up(4).south(), cactus.withProperty(BlockLog.LOG_AXIS, EnumAxis.Z), 2);

                    for(int k = 0; k < 3; ++k) {
                        world.setBlockState(pos1.north(2).up(3 + k), cactus, 2);
                        world.setBlockState(pos1.south(2).up(4 + k), cactus, 2);
                    }
                }else{
                    world.setBlockState(pos1.up(3).east(), cactus.withProperty(BlockLog.LOG_AXIS, EnumAxis.X), 2);
                    world.setBlockState(pos1.up(4).west(), cactus.withProperty(BlockLog.LOG_AXIS, EnumAxis.X), 2);

                    for(int k = 0; k < 3; ++k) {
                        world.setBlockState(pos1.east(2).up(3 + k), cactus, 2);
                        world.setBlockState(pos1.west(2).up(4 + k), cactus, 2);
                    }
                }
            }
        }

        WorldGens.addBaseGenNew(ice, world, rand, pos.getX(), pos.getZ(), 120, 30, 150);
        WorldGens.addBaseGenNew(packedIce, world, rand, pos.getX(), pos.getZ(), 100, 20, 150);
    }
}