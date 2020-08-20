package degubi.teamcraft.block;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockHalfBlocks extends Block {
    private static final AxisAlignedBB[] bounds = new AxisAlignedBB[]{new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F), new AxisAlignedBB(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), new AxisAlignedBB(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F), new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F)};
    static final HashMap<Block, Block> returnBlock = new HashMap<>();
    
    private final Block modelBlock;
    
    public BlockHalfBlocks(Block infBlock){
        super(infBlock.getDefaultState().getMaterial());
        this.modelBlock = infBlock;
        
        setCreativeTab(Main.tabBlocks);
        setHardness(infBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(infBlock.getExplosionResistance(null, null, null, null));
        setSoundType(infBlock.getSoundType(null, null, null, null));
        setHarvestLevel(infBlock.getHarvestTool(infBlock.getDefaultState()), infBlock.getHarvestLevel(infBlock.getDefaultState()));
        setDefaultState(getBlockState().getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        setLightOpacity(255);
        if(infBlock == Blocks.ICE || infBlock == Blocks.PACKED_ICE){
            setDefaultSlipperiness(0.98F);
        }
        returnBlock.put(infBlock, this);
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return this.modelBlock.getBlockLayer();
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return facing == EnumFacing.UP || facing != EnumFacing.DOWN ? getDefaultState().withProperty(BlockHorizontal.FACING, placer.getHorizontalFacing()) : getDefaultState().withProperty(BlockHorizontal.FACING, facing);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        return EnumFacing.getFront(meta).getAxis() == EnumFacing.Axis.Y ? getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH) : getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.getFront(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(BlockHorizontal.FACING).getIndex();
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(!heldItem.isEmpty() && heldItem.getItem() == Main.tcmMultiTool){
            world.setBlockState(pos, state.cycleProperty(BlockHorizontal.FACING));
            return true;
        }
        return false;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face){
        return state.getValue(BlockHorizontal.FACING).getOpposite() == face ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess acc, BlockPos pos){
        return bounds[state.getValue(BlockHorizontal.FACING).getHorizontalIndex()];
    }
}