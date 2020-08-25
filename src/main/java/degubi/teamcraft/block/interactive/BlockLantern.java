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
import net.minecraftforge.fml.relauncher.*;

public final class BlockLantern extends Block {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3D, 0, 0.3D, 0.7D, 1, 0.7D);

    public BlockLantern(){
        super(Material.ROCK);

        Block modelBlock = Blocks.STONE;
        setDefaultState(getBlockState().getBaseState().withProperty(BlockLantern.ACTIVE, Boolean.FALSE));
        setCreativeTab(Main.tabDecorations);
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BOUNDING_BOX;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess acc, BlockPos pos) {
        return state.getValue(ACTIVE).booleanValue() ? 15 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(BlockLantern.ACTIVE, Boolean.valueOf(meta == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(BlockLantern.ACTIVE).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockLantern.ACTIVE);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        world.setBlockState(pos, state.cycleProperty(BlockLantern.ACTIVE));
        return true;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(Main.Lantern));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Main.Lantern);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand){
        if(state.getValue(BlockLantern.ACTIVE).booleanValue()){
            double d0 = pos.getX() + 0.5F;
            double d1 = pos.getY() + 0.35F;
            double d2 = pos.getZ() + 0.5F;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}