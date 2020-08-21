package degubi.teamcraft.block;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockRotatingBlocks extends BlockLog{
    private static final PotionEffect eff = new PotionEffect(MobEffects.POISON, 400, 2);
        
    public BlockRotatingBlocks() {
        setCreativeTab(Main.tabBlocks);
        setDefaultState(getBlockState().getBaseState().withProperty(LOG_AXIS, EnumAxis.Y));
    }
    
    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        return false;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(LOG_AXIS).ordinal();
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
        if(state.getBlock() == Main.Cactus){
            entity.attackEntityFrom(DamageSource.CACTUS, 2.0F);

            if(entity instanceof EntityLivingBase){
                ((EntityLivingBase) entity).addPotionEffect(eff);
            }
        }
    }
}