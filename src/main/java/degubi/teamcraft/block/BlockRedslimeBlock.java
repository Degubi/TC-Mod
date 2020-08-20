package degubi.teamcraft.block;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockRedslimeBlock extends BlockSlime{
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0.01F, 0.01f, 0.01, 0.99f, 0.99f, 0.99f);
    
    public BlockRedslimeBlock(){
        setCreativeTab(Main.tabDecorations);
        setHardness(Blocks.SLIME_BLOCK.getDefaultState().getBlockHardness(null, null));
        setResistance(Blocks.SLIME_BLOCK.getExplosionResistance(null, null, null, null));
        setSoundType(Main.deepSlime);
        setHarvestLevel(Blocks.SLIME_BLOCK.getHarvestTool(Blocks.SLIME_BLOCK.getDefaultState()), Blocks.SLIME_BLOCK.getHarvestLevel(Blocks.SLIME_BLOCK.getDefaultState()));
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BOX;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
        if(entity instanceof EntityLivingBase){
            if(entity.isSprinting()){
                entity.motionY += 3.0D;
                entity.playSound(SoundEvents.ENTITY_SMALL_SLIME_JUMP, 10.0F, 1.0F);
            }else if(entity.isSneaking()){
                entity.motionY = 0.0D;
            }else{
                entity.motionY+= 2.0D;
                entity.playSound(SoundEvents.BLOCK_SLIME_STEP, 10.0F, 1.0F);
            }
        }
    }
}