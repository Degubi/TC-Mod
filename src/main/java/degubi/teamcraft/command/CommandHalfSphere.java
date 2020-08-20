package degubi.teamcraft.command;

import com.google.common.collect.*;
import java.util.*;
import net.minecraft.command.*;
import net.minecraft.init.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

public class CommandHalfSphere extends CommandBase{

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
    
    @Override
    public String getName() {
        return "/halfsphere";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "halfsphere <radius>";
    }
    
    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        BlockPos pos = sender.getPosition();
        
        if(args.length > 0){
            int radius = Integer.parseInt(args[0]);
            for(int r = 0; r < radius; r++){
                for(int x = -r; x < r; x++){
                    for(int y = 0; y < r; y++){
                        for(int z = -r; z < r; z++){
                            double dist = MathHelper.sqrt((x*x + y*y + z*z));
                            
                            if(dist > r)
                                continue;
                            sender.getEntityWorld().setBlockState(new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z), Blocks.BEDROCK.getDefaultState(), 2);
                        }
                    }
                }
            }
        }else{
            sender.sendMessage(new TextComponentString("Add radius argument!"));
        }
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return Lists.newArrayList();
    }
    
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}