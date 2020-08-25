package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public final class BlockMeatBlock extends Block {
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 7);
    private static final AxisAlignedBB[] MEAT_AAB = new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.95D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};

    public BlockMeatBlock() {
        super(Material.CAKE);

        Block modelBlock = Blocks.CAKE;
        setDefaultState(blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
        setCreativeTab(Main.tabFood);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess acc, BlockPos pos){
        return MEAT_AAB[state.getValue(BITES).intValue()];
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        PlussHeal(world, pos, state, player);
        return true;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player){
        PlussHeal(world, pos, world.getBlockState(pos), player);
    }

    private static void PlussHeal(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        if (player.canEat(false)){
            player.getFoodStats().addStats(8, 0.6F);
            int l = state.getValue(BITES).intValue();

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), Main.MEATBLOCK_EAT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            if (l >= 7){
                world.setBlockToAir(pos);
            }else{
                world.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(l + 1)), 3);
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(BITES, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(BITES).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BITES);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemById(0);
    }
}