package degubi.teamcraft.item;

import com.google.common.collect.*;
import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.BlockDirt.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class ItemMultiTool extends ItemTool{
    public ItemMultiTool(ToolMaterial material) {
        super(material, ImmutableSet.of(Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL, Blocks.SNOW_LAYER, Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND, Blocks.SOUL_SAND, Blocks.MYCELIUM, Blocks.WEB, Blocks.PUMPKIN, Blocks.MELON_BLOCK));
        setCreativeTab(Main.ToolsWeapons);
    }
    
    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("sword");
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState block){
        return block.getBlock() == Blocks.WEB || block.getBlock()  == Blocks.SNOW_LAYER || block.getBlock()  == Blocks.OBSIDIAN ? toolMaterial.getHarvestLevel() == 3 : (block.getBlock() != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE ? (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK ? (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE ? (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE ? (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE ? (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE ? (block.getMaterial() == Material.ROCK ? true : (block.getMaterial() == Material.IRON ? true : block.getMaterial() == Material.ANVIL)) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2);
    }
    
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState block){
        return block == Blocks.WEB.getDefaultState() ? 10.0F : (block.getMaterial() != Material.WOOD && block.getMaterial() != Material.PLANTS && block.getMaterial() != Material.VINE && block.getMaterial() != Material.IRON && block.getMaterial() != Material.ANVIL && block.getMaterial() != Material.ROCK ? super.getDestroySpeed(stack, block) : efficiency);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)){
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate = world.getBlockState(pos);

        if(facing != EnumFacing.DOWN && world.isAirBlock(pos.up())){
            if(iblockstate.getBlock() == Blocks.DIRT){
                if(iblockstate.getValue(BlockDirt.VARIANT) == DirtType.DIRT) {
                    setBlock(itemstack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                }else if(iblockstate.getValue(BlockDirt.VARIANT) == DirtType.COARSE_DIRT) {
                    setBlock(itemstack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                }
                return EnumActionResult.SUCCESS;
            }else if(iblockstate.getBlock() == Blocks.GRASS){
                setBlock(itemstack, player, world, pos, Blocks.GRASS_PATH.getDefaultState());
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
    
    private static void setBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state){
        world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        
        if(!world.isRemote){
            world.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }
}