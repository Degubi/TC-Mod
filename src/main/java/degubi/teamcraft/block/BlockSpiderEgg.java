package degubi.teamcraft.block;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockSpiderEgg extends BlockDragonEgg{

    public BlockSpiderEgg() {
        setCreativeTab(Main.tabDecorations);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        Surprise(world, pos);
        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player){
        Surprise(world, pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
        if(!world.isRemote && entity instanceof EntityArrow){
            Surprise(world, pos);
        }
    }

    private static void Surprise(World world, BlockPos pos){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if(world.getGameRules().getBoolean("mobGriefing")){
            if(!world.isRemote){
                EntityCaveSpider spider = new EntityCaveSpider(world);
                EntityCaveSpider spider2 = new EntityCaveSpider(world);
                spider.setLocationAndAngles(x, y + 0.5D, z, world.rand.nextFloat() * 360.0F, 0.0F);
                spider2.setLocationAndAngles(x, y + 0.5D, z, world.rand.nextFloat() * 360.0F, 0.0F);

                world.createExplosion(spider, x, y, z, 2.0F, true);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                world.spawnEntity(spider);
                world.spawnEntity(spider2);
            }
        }else{
            world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, Main.SOUNDBLOCK_5, SoundCategory.BLOCKS, 1, 1, true);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}