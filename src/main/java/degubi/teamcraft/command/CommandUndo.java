package degubi.teamcraft.command;

import degubi.teamcraft.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;

public class CommandUndo extends CommandBase{

    @Override
    public String getName() {
        return "pundo";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "/pundo";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if (args.length <= 0){
            throw new CommandException("Add file Name!");
        }
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Main.GenerationItem && player.getHeldItemMainhand().hasTagCompound()){
            //    NBTTagCompound tag = StructureHelper.readCompressedTag(args[0] + "_backup");
            //    int blockCount = tag.getInteger(StructureHelper.BLOCKCOUNT);
                
                /*for(int ID = 1; ID <= blockCount; ++ ID){
                    BlockPos setPos = StructureHelper.getPosFromTag(tag, StructureHelper.BLOCKDATA, ID);
                    IBlockState block = StructureHelper.getBlockFromID(tag, StructureHelper.BLOCKDATA, ID).getDefaultState();
                    if(StructureHelper.hasMetadata(tag, ID)){
                        int meta = tag.getIntArray(StructureHelper.BLOCKDATA + ID)[4];
                        sender.getEntityWorld().setBlockState(setPos, block.getBlock().getStateFromMeta(meta));
                    }else{
                        sender.getEntityWorld().setBlockState(setPos, block);
                    }
                }*/
            }
        }
    }
}