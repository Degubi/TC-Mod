package degubi.teamcraft.command;

import com.google.common.collect.*;
import degubi.teamcraft.*;
import java.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;

public class CommandClear extends CommandBase {
    
    @Override
    public String getName() {
        return "pclear";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "/pclear <tag>";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("-all")){
                StructureHelper.clearExportFolder(0, (EntityPlayer) sender);
            }else if(args[0].equalsIgnoreCase("-normal")){
                StructureHelper.clearExportFolder(1, (EntityPlayer) sender);
            }else if(args[0].equalsIgnoreCase("-backup")){
                StructureHelper.clearExportFolder(2, (EntityPlayer) sender);
            }
        }else{
            throw new WrongUsageException("pclear <tags>");
        }
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? Lists.newArrayList("-all", "-normal", "-backup") : Lists.newArrayList();
    }
}