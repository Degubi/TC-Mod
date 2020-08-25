package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.gui.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class BlockRedstoneTimer extends Block {
    public static final PropertyBool isPowered = PropertyBool.create("ispowered");
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);

    public BlockRedstoneTimer() {
        super(Material.CIRCUITS);

        Block modelBlock = Blocks.UNPOWERED_REPEATER;
        setDefaultState(getBlockState().getBaseState().withProperty(isPowered, Boolean.FALSE));
        setCreativeTab(Main.tabRedstone);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    public boolean canProvidePower(IBlockState state){
        return true;
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
    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(isPowered, Boolean.valueOf(meta == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(isPowered).booleanValue() ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, isPowered);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        world.notifyNeighborsRespectDebug(pos, this, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRedstoneTimer(pos));
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess acc, BlockPos pos, EnumFacing side){
        return state.getValue(isPowered).booleanValue() ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTimer();
    }
}