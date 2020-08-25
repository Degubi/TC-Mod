package degubi.teamcraft.command;

import com.google.common.collect.*;
import java.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.math.*;

public class CommandExpand extends CommandBase{

    @Override
    public String getName() {
        return "pexpand";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/pexpand <tag>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if(args.length > 0){
            if(sender instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) sender;
                if(player.getHeldItemMainhand().hasTagCompound() && player.getHeldItemMainhand().getTagCompound().hasKey("pos1") && player.getHeldItemMainhand().getTagCompound().hasKey("pos2")){
                    NBTTagCompound tag = player.getHeldItemMainhand().getTagCompound();
            if(args[0].equalsIgnoreCase("-up")){
                if(args[1] != null && NumberUtils.isCreatable(args[1])){
                    tag.setIntArray("pos1", new int[]{tag.getIntArray("pos1")[0], tag.getIntArray("pos1")[1] + Integer.parseInt(args[1]), tag.getIntArray("pos1")[2]});
                }else{
                    tag.setIntArray("pos1", new int[]{tag.getIntArray("pos1")[0], 256, tag.getIntArray("pos1")[2]});
                }
                player.sendMessage(new TextComponentString("New positions: " + tag.getIntArray("pos1")[0] + " " + tag.getIntArray("pos1")[1] + " " + tag.getIntArray("pos1")[2]));
            }else if(args[0].equalsIgnoreCase("-down")){
                if(args[1] != null && NumberUtils.isCreatable(args[1])){
                    tag.setIntArray("pos2", new int[]{tag.getIntArray("pos2")[0], tag.getIntArray("pos2")[1] + Integer.parseInt(args[1]), tag.getIntArray("pos2")[2]});
                }else{
                    tag.setIntArray("pos2", new int[]{tag.getIntArray("pos2")[0], 0, tag.getIntArray("pos2")[2]});
                }
                player.sendMessage(new TextComponentString("New positions: " + tag.getIntArray("pos2")[0] + " " + tag.getIntArray("pos2")[1] + " " + tag.getIntArray("pos2")[2]));
            }
            }else{
                throw new WrongUsageException("clearexports <tags>");
            }
            }
        }else{
            throw new WrongUsageException("pexpand <tags>");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? Lists.newArrayList("-up", "-down") : Lists.newArrayList();
    }
}