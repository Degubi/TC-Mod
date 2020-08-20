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

public final class Slabs2 extends BlockSlab{
    private static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", Slabs2.EnumType.class);
    private final boolean isDouble;
    
    public Slabs2(boolean isD){
        super(Material.WOOD);
        isDouble = isD;
        setCreativeTab(Main.tabBlocks);
        setSoundType(SoundType.WOOD);
        IBlockState iblockstate = blockState.getBaseState();
        if(!isDouble()){
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        setHardness(2.0F);
        setDefaultState(iblockstate.withProperty(VARIANT, EnumType.birchlog));
    }
    
    @Override
    public boolean isDouble() {return isDouble;}
    
    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumType.VALUES[stack.getItemDamage()];
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return Item.getItemFromBlock(Main.SingleSlabs2);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(Item.getItemFromBlock(Main.SingleSlabs2), 1, state.getValue(VARIANT).ordinal());
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
            iblockstate = iblockstate.withProperty(BlockSlab.HALF, (meta & 7) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
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
        oaklog,
        sprucelog,
        birchlog,
        junglelog,
        acacialog,
        darkoaklog,
        retard,
        andesitestonebrick;

        public static final EnumType[] VALUES = values();
        
        private EnumType() {
        }
        
        @Override
        public String getName(){
            return name();
        }
    }
}