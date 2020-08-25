package degubi.teamcraft.block.interactive;

import degubi.teamcraft.*;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;

public final class BlockSoundBlocks extends Block {
    private final SoundEvent soundToPlay;
    public static final PropertyInteger volume = PropertyInteger.create("volume", 0, 5);
    public static final PropertyBool redstoneState = PropertyBool.create("redstonestate");

    public BlockSoundBlocks(SoundEvent sound) {
        super(Blocks.STONE.getDefaultState().getMaterial());
        soundToPlay = sound;

        Block modelBlock = Blocks.STONE;
        setDefaultState(blockState.getBaseState().withProperty(volume, Integer.valueOf(1)).withProperty(redstoneState, Boolean.FALSE));
        setCreativeTab(Main.tabAdmin);
        setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
        setResistance(modelBlock.getExplosionResistance(null, null, null, null));
        setSoundType(modelBlock.getSoundType(null, null, null, null));
        setHarvestLevel(modelBlock.getHarvestTool(modelBlock.getDefaultState()), modelBlock.getHarvestLevel(modelBlock.getDefaultState()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return meta > 5 ? getDefaultState().withProperty(volume, Integer.valueOf(meta - 6)).withProperty(redstoneState, Boolean.TRUE) : getDefaultState().withProperty(volume, Integer.valueOf(meta)).withProperty(redstoneState, Boolean.FALSE);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand){
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return state.getValue(redstoneState).booleanValue() ? state.getValue(volume).intValue() + 6 : state.getValue(volume).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, volume, redstoneState);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
        boolean flag = world.isBlockPowered(pos);

        if(state.getValue(redstoneState).booleanValue() != flag){
            if(flag){
                world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, soundToPlay, SoundCategory.BLOCKS, state.getValue(volume).intValue() / 5.0F, 1, true);
            }
            world.setBlockState(pos, state.withProperty(redstoneState, flag ? Boolean.TRUE : Boolean.FALSE));
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() == Main.MultiTool){
            world.setBlockState(pos, state.cycleProperty(volume));

            if(world.isRemote){
                player.sendMessage(new TextComponentString("Volume is " + state.getValue(volume).intValue()));
            }
            return true;
        }

        world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, soundToPlay, SoundCategory.BLOCKS, state.getValue(volume).intValue() / 5.0F, 1, true);
        return true;
    }
}