package degubi.teamcraft.biome;

import degubi.teamcraft.worldgen.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.relauncher.*;

public final class BiomeNiceShotForest extends Biome{
    private static final WorldGenNS1 tree1 = new WorldGenNS1();
    private static final WorldGenNS2 tree2 = new WorldGenNS2();

    public BiomeNiceShotForest() {
        super(new BiomeProperties("nsforest"));
        decorator.treesPerChunk = 25;
        decorator.grassPerChunk = 30;
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand){
        return rand.nextInt(20) > 15 ? tree1 : tree2;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x91ff0f;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x91ff0f;
    }
}