package degubi.teamcraft.network;

import degubi.teamcraft.block.tile.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class HandlerRedstoneTimer implements IMessageHandler<MessageCoordInt, IMessage> {

    @Override
    public IMessage onMessage(MessageCoordInt message, MessageContext ctx) {
        TileEntityTimer tile = (TileEntityTimer) ctx.getServerHandler().player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
        int type = message.messageType;

        if(type == 0) {
            tile.switchActiveState();
        }else if(type == 1){
            tile.incMaxCounter();
        }else{
            tile.decMaxCounter();
        }

        return null;
    }
}