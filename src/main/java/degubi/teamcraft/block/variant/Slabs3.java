package degubi.teamcraft.block.variant;

import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public final class Slabs3 extends BlockSlab{
    private static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", Slabs3.EnumType.class);
    private final boolean isDouble;
    
    public Slabs3(boolean isD){
        super(Material.ROCK);
        isDouble = isD;
        setCreativeTab(Main.tabBlocks);
        IBlockState iblockstate = blockState.getBaseState();
        if(!isDouble()){
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        setHardness(2.0F);
        setDefaultState(iblockstate.withProperty(VARIANT, EnumType.gravelstone));
    }
    
    @Override
    public boolean isDouble() {
        return isDouble;
    }
    
    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumType.VALUES[stack.getItemDamage()];
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Main.SingleSlabs3);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Item.getItemFromBlock(Main.SingleSlabs3), 1, state.getValue(VARIANT).ordinal());
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return state.getValue(VARIANT) == Slabs3.EnumType.ice ? false : super.doesSideBlockRendering(state, world, pos, face);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess acc, BlockPos pos, EnumFacing side) {
        IBlockState neighbros = acc.getBlockState(pos.offset(side));
        return neighbros == state || neighbros.isFullCube() ? false : super.shouldSideBeRendered(state, acc, pos, side);
    }
    
    @Override
    public int damageDropped(IBlockState state){
        return state.getValue(VARIANT).ordinal();
    }
    
    @Override
    public String getUnlocalizedName(int meta){
        return "tile." + EnumType.VALUES[meta].getName().toLowerCase() + "slab";
    }
    
    @Override
    public IProperty<EnumType> getVariantProperty(){
        return VARIANT;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list){
        for(int i = 0; i < 8; ++ i) list.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta){
        IBlockState iblockstate = getDefaultState().withProperty(VARIANT, EnumType.VALUES[meta & 7]);
        if(!isDouble()){
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }
        return iblockstate;
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        int i = 0 | state.getValue(VARIANT).ordinal();
        if (!isDouble() && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP){
            i |= 8;
        }
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, BlockSlab.HALF, VARIANT);
    }
    
    private static enum EnumType implements IStringSerializable{
        packedice,
        ice,
        marblebrick,
        darkprismarine,
        gravelstone,
        smoothsandstone,
        crackedstonebrick,
        granitestonebrick;

        public static final EnumType[] VALUES = values();
        
        private EnumType() {
        }
        
        @Override
        public String getName(){
            return name();
        }
    }
}