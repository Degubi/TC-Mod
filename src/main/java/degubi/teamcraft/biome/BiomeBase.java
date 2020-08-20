package degubi.teamcraft.biome;

import degubi.teamcraft.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import net.minecraftforge.fml.relauncher.*;

public final class BiomeBase extends Biome{
    
    public BiomeBase(String name, float baseHeight) {
        super(new BiomeProperties(name).setTemperature(0.0F).setRainfall(0.1F).setBaseHeight(baseHeight).setHeightVariation(6.0F));
        fillerBlock = Blocks.STONE.getDefaultState();
        topBlock = Blocks.GRASS.getDefaultState();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos){
        return this == Main.extremestHill ? 0x54a0ff : super.getGrassColorAtPos(pos);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos){
        return getGrassColorAtPos(pos);
    }
}