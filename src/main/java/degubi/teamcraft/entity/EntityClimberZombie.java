package degubi.teamcraft.entity;

import net.minecraft.block.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.pathfinding.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
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

        World world = this.world;
        if(!world.isRemote && getAttackTarget() != null && collidedHorizontally) {
            BlockPos position = getPosition();
            EnumFacing facing = getHorizontalFacing();

            if(world.getBlockState(position.offset(facing)).isFullBlock() && world.isAirBlock(position)) {
                world.setBlockState(position, Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, facing.getOpposite()));
            }
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