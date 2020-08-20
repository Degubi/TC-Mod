package degubi.teamcraft.command;

import degubi.teamcraft.*;
import java.time.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

public class CommandExport extends CommandBase{
    
    @Override
    public String getName(){
        return "pexport";
    }
    
    @Override
    public String getUsage(ICommandSender sender){
        return "/pexport <name>";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if(args.length <= 0){
            throw new CommandException("Add file Name!");
        }
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            
            if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Main.PositionItem){
                ItemStack stack = player.getHeldItemMainhand();
                
                if(stack.hasTagCompound() && stack.getTagCompound().hasKey("pos1") && stack.getTagCompound().hasKey("pos2")){
                    LocalTime time = LocalTime.now();
                    int seconds = time.getSecond();
                    int miliseconds = time.getNano();
                    NBTTagCompound tag = stack.getTagCompound();
                    
                    BlockPos blockpos = StructureHelper.getPosFromTag(tag, "pos", 1);
                    BlockPos blockpos1 = StructureHelper.getPosFromTag(tag, "pos", 2);
                    
                    BlockPos startPos = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
                    BlockPos endPos = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
                    
                    NBTTagCompound setTag = new NBTTagCompound();
                    int ID = 0;
                    player.sendMessage(new TextComponentString("Please wait..."));
                    for(int k1 = startPos.getX(); k1 <= endPos.getX(); ++k1){
                        for(int i1 = startPos.getZ(); i1 <= endPos.getZ(); ++i1){
                            for(int j1 = startPos.getY(); j1 <= endPos.getY(); ++j1){
                                BlockPos foreachPos = new BlockPos(k1, j1, i1);
                                
                                if(player.getEntityWorld().getBlockState(foreachPos).getBlock() != Blocks.AIR){
                                    ++ID;
                                    IBlockState state = player.getEntityWorld().getBlockState(foreachPos);
                                    
                                    setTag.setString(StructureHelper.BLOCKNAME + ID, state.getBlock().getRegistryName().toString());
                                    setTag.setIntArray(StructureHelper.BLOCKCOORDS + ID, new int[] {foreachPos.getX(), foreachPos.getY(), foreachPos.getZ()});
                                    if(!state.getProperties().isEmpty()) {
                                        int propCounter = 0;
                                        for(IProperty<?> kek : state.getPropertyKeys()) {
                                            setTag.setString(StructureHelper.BLOCKPROPERTIES + ID + "_" + propCounter, state.getValue(kek).toString());
                                            ++propCounter;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    setTag.setInteger(StructureHelper.BLOCKCOUNT, ID);
                    StructureHelper.writeCompressedTag(setTag, args[0]);
                    player.sendMessage(new TextComponentString("Done! Blocks copied: " + ID));
                    player.sendMessage(new TextComponentString("Time elapsed: " + String.valueOf(Math.max(time.getSecond(), seconds) - Math.min(time.getSecond(), seconds)) + " seconds and " + String.valueOf(Math.max(time.getNano(), miliseconds) - Math.min(time.getNano(), miliseconds)) + " miliseconds"));
                    }else{
                        throw new CommandException("Must set Positions!");
                    }
                }else{
                    throw new CommandException("Must hold a Position Item!");
            }
        }
    }
    /*
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if(args.length <= 0){
            throw new CommandException("Add file Name!");
        }
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Main.PositionItem){
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.hasTagCompound() && stack.getTagCompound().hasKey("pos1") && stack.getTagCompound().hasKey("pos2")){
                    LocalTime time = LocalTime.now();
                    int seconds = time.getSecond();
                    int miliseconds = time.getNano();
                    
                    NBTTagCompound tag = stack.getTagCompound();
                    BlockPos blockpos = StructureHelper.getPosFromTag(tag, "pos", 1);
                    BlockPos blockpos1 = StructureHelper.getPosFromTag(tag, "pos", 2);
                    
                    BlockPos startPos = new BlockPos(Math.min(blockpos.getX(), blockpos1.getX()), Math.min(blockpos.getY(), blockpos1.getY()), Math.min(blockpos.getZ(), blockpos1.getZ()));
                    BlockPos endPos = new BlockPos(Math.max(blockpos.getX(), blockpos1.getX()), Math.max(blockpos.getY(), blockpos1.getY()), Math.max(blockpos.getZ(), blockpos1.getZ()));
                    
                    NBTTagCompound setTag = new NBTTagCompound();
                    int ID = 0;
                    player.sendMessage(new TextComponentString("Please wait..."));
                    for(int k1 = startPos.getX(); k1 <= endPos.getX(); ++k1){
                        for(int i1 = startPos.getZ(); i1 <= endPos.getZ(); ++i1){
                            for(int j1 = startPos.getY(); j1 <= endPos.getY(); ++j1){
                                BlockPos foreachPos = new BlockPos(k1, j1, i1);
                                
                                if(player.getEntityWorld().getBlockState(foreachPos).getBlock() != Blocks.AIR){
                                    ++ID;
                                    IBlockState state = player.getEntityWorld().getBlockState(foreachPos);
                                    
                                    setTag.setString(StructureHelper.BLOCKNAME + ID, state.getBlock().getRegistryName().toString());
                                    setTag.setIntArray(StructureHelper.BLOCKCOORDS + ID, new int[] {foreachPos.getX(), foreachPos.getY(), foreachPos.getZ()});
                                    if(!state.getProperties().isEmpty()) {
                                        int propCounter = 0;
                                        for(IProperty kek : state.getPropertyKeys()) {
                                            setTag.setString(StructureHelper.BLOCKPROPERTIES + ID + "_" + propCounter, state.getValue(kek).toString());
                                            ++propCounter;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    setTag.setInteger(StructureHelper.BLOCKCOUNT, ID);
                    StructureHelper.writeCompressedTag(setTag, args[0]);
                    player.sendMessage(new TextComponentString("Done! Blocks copied: " + ID));
                    player.sendMessage(new TextComponentString("Time elapsed: " + String.valueOf(Math.max(time.getSecond(), seconds) - Math.min(time.getSecond(), seconds)) + " seconds and " + String.valueOf(Math.max(time.getNano(), miliseconds) - Math.min(time.getNano(), miliseconds)) + " miliseconds"));
                    }else{
                        throw new CommandException("Must set Positions!");
                    }
                }else{
                    throw new CommandException("Must hold a Position Item!");
            }
        }
    }
    */
}