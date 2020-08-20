package degubi.teamcraft.block;

import com.google.common.collect.*;
import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.BlockPlanks.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockAppleLeaves extends BlockLeaves {
    public static final PropertyBool hasApple = PropertyBool.create("hasapple");

    public BlockAppleLeaves(){
        setCreativeTab(Main.tabDecorations);
        setDefaultState(blockState.getBaseState().withProperty(hasApple, Boolean.FALSE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.TRUE).withProperty(BlockLeaves.DECAYABLE, Boolean.FALSE));
    }
    
    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune){
        return Lists.newArrayList(new ItemStack(this));
    }
    
    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(5) == 3 ? 1 : 0;
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
        super.updateTick(world, pos, state, rand);
        if(!world.isRemote && !state.getValue(hasApple).booleanValue()){
            if(rand.nextInt(50) == 3){
                world.setBlockState(pos, state.cycleProperty(hasApple));
            }
        }
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return state.getBlock() == Main.AppleLeaves ? Item.getItemFromBlock(Main.AppleSapling) : Item.getItemFromBlock(Main.bananaSapling);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return Minecraft.isFancyGraphicsEnabled() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }
    
    @Override
    public EnumType getWoodType(int meta){
        return EnumType.OAK;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(hasApple, Boolean.valueOf((meta & 2) == 0)).withProperty(BlockLeaves.DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, hasApple, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(state.getValue(hasApple).booleanValue()){
            if(state.getBlock() == Main.AppleLeaves) {
                player.inventory.addItemStackToInventory(new ItemStack(Items.APPLE));
            }else {
                player.inventory.addItemStackToInventory(new ItemStack(Main.Banana));
            }
            world.setBlockState(pos, state.cycleProperty(hasApple));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return !Minecraft.isFancyGraphicsEnabled();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
        return !Minecraft.isFancyGraphicsEnabled() && blockAccess.getBlockState(pos.offset(side)) == blockState ? false : true;
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        int i = 0;
        
        if (!state.getValue(hasApple).booleanValue()){
            i |= 2;
        }
        if (!state.getValue(BlockLeaves.DECAYABLE).booleanValue()){
            i |= 4;
        }
        if (state.getValue(BlockLeaves.CHECK_DECAY).booleanValue()){
            i |= 8;
        }
        return i;
    }
}