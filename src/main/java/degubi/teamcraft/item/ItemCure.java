package degubi.teamcraft.item;

import degubi.teamcraft.*;
import degubi.teamcraft.entity.*;
import net.minecraft.block.*;
import net.minecraft.dispenser.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public final class ItemCure extends Item {

    public ItemCure(){
        setMaxStackSize(1);
        setCreativeTab(Main.ToolsWeapons);

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, (source, stack) -> {
            World world = source.getWorld();
            IPosition pos = BlockDispenser.getDispensePosition(source);
            EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
            IProjectile iprojectile = new EntityCureItem(world, pos.getX(), pos.getY(), pos.getZ());

            iprojectile.shoot(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1F, enumfacing.getFrontOffsetZ(), 1.1F, 6.0F);
            world.spawnEntity((Entity)iprojectile);
            stack.shrink(1);
            return stack;
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
        if(!world.isRemote){
            EntityCureItem grenade = new EntityCureItem(world, player);
            grenade.shoot(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.spawnEntity(grenade);
        }

        if(!player.capabilities.isCreativeMode){
            player.inventory.getCurrentItem().shrink(1);
        }

        return super.onItemRightClick(world, player, hand);
    }
}