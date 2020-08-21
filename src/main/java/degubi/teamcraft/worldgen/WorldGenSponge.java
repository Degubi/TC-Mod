package degubi.teamcraft.worldgen;

import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

public final class WorldGenSponge extends WorldGenerator {
    private final Block getBlock;
    private final IBlockState setBlock;
    private final Material material;
    
    public WorldGenSponge(IBlockState set, Block get, Material mat) {
        setBlock = set;
        getBlock = get;
        material = mat;
    }
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos){
        if(world.getBlockState(pos).getMaterial() != material){
            return false;
        }
        int i = 2+rand.nextInt(3);
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        MutableBlockPos blockPos = new MutableBlockPos();
        
        for(int j = posX - i; j <= posX + i; ++j){
            for(int k = posZ - i; k <= posZ + i; ++k){
                int l = j - posX;
                int i1 = k - posZ;
                
                if(l * l + i1 * i1 <= i * i){
                    for(int j1 = posY - 1; j1 <= posY + 1; ++j1){
                        blockPos.setPos(j, j1, k);
                        
                        if(world.getBlockState(blockPos).getBlock() == getBlock){
                            world.setBlockState(blockPos, setBlock, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}