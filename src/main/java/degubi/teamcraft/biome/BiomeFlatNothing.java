package degubi.teamcraft.biome;

import net.minecraft.block.*;
import net.minecraft.block.BlockDirt.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.*;
import net.minecraft.world.biome.*;

public final class BiomeFlatNothing extends Biome {

    public BiomeFlatNothing() {
        super(new BiomeProperties("FlatNothing").setRainDisabled().setTemperature(2.0F).setRainfall(0.0F).setBaseHeight(0.0F).setHeightVariation(0.0F));

        topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.COARSE_DIRT);
        fillerBlock = Blocks.DIRT.getDefaultState();
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 2, 1, 3));
        decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 4;
        decorator.flowersPerChunk = 0;
        decorator.deadBushPerChunk = 5;
        decorator.sandPatchesPerChunk = 0;
        decorator.clayPerChunk = 0;
    }
}