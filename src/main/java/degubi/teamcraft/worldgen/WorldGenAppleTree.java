package degubi.teamcraft.worldgen;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

public final class WorldGenAppleTree extends WorldGenAbstractTree {

    public WorldGenAppleTree() {
        super(false);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position){
        int i = rand.nextInt(3) + 5;
        int posY = position.getY();
        int posX = position.getX();
        int posZ = position.getZ();
        int worldHeight = world.getHeight();

        if (posY >= 1 && posY + i + 1 <= worldHeight){
            boolean flag = true;
            for (int j = posY; j <= posY + 1 + i; ++j){
                int k = 1;

                if (j == posY){
                    k = 0;
                }
                if (j >= posY + 1 + i - 2){
                    k = 2;
                }
                for (int l = posX - k; l <= posX + k && flag; ++l){
                    for (int i1 = posZ - k; i1 <= posZ + k && flag; ++i1){
                        if (j >= 0 && j < worldHeight){
                            if (!this.isReplaceable(world, new BlockPos(l, j, i1))){
                                flag = false;
                            }
                        }else{
                            flag = false;
                        }
                    }
                }
            }

            if (!flag){
                return false;
            }
            IBlockState state = world.getBlockState(position.down());

            if (state.getBlock().canSustainPlant(state, world, position.down(), EnumFacing.UP, (BlockSapling)Blocks.SAPLING) && position.getY() < world.getHeight() - i - 1){
                this.setDirtAt(world, position.down());
                int k2 = 3;
                int l2 = 0;

                for (int i3 = posY - k2 + i; i3 <= posY + i; ++i3){
                    int i4 = i3 - (posY + i);
                    int j1 = l2 + 1 - i4 / 2;

                    for (int k1 = posX - j1; k1 <= posX + j1; ++k1){
                        int l1 = k1 - posX;

                        for (int i2 = posZ - j1; i2 <= posZ + j1; ++i2){
                            int j2 = i2 - posZ;

                            if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i4 != 0){
                                BlockPos blockpos = new BlockPos(k1, i3, i2);
                                state = world.getBlockState(blockpos);

                                if (state.getBlock().isAir(state, world, blockpos) || state.getBlock().isLeaves(state, world, blockpos) || state.getMaterial() == Material.VINE){
                                    this.setBlockAndNotifyAdequately(world, blockpos, Main.AppleLeaves.getDefaultState());
                                }
                            }
                        }
                    }
                }

                for (int j3 = 0; j3 < i; ++j3){
                    BlockPos upN = position.up(j3);
                    state = world.getBlockState(upN);

                    if (state.getBlock().isAir(state, world, upN) || state.getBlock().isLeaves(state, world, upN) || state.getMaterial() == Material.VINE){
                        this.setBlockAndNotifyAdequately(world, position.up(j3), Blocks.LOG.getDefaultState());
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}