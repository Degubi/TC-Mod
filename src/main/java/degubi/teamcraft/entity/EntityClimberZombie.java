package degubi.teamcraft.entity;

import net.minecraft.block.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.pathfinding.*;
import net.minecraft.world.*;

public final class EntityClimberZombie extends EntityZombie{
    
    public EntityClimberZombie(World theWorld) {
        super(theWorld);
        setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Blocks.LADDER));
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && getAttackTarget() != null && collidedHorizontally && world.getBlockState(getPosition().offset(getHorizontalFacing())).isFullBlock()){
            world.setBlockState(getPosition(), Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, getHorizontalFacing().getOpposite()));
        }
    }
    
    @Override
    public boolean isOnLadder() {
        return true;
    }
    
    @Override
    protected PathNavigate createNavigator(World worldIn){
        return new PathNavigateClimber(this, worldIn);
    }
}