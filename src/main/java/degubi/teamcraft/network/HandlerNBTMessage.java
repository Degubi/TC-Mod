package degubi.teamcraft.network;

import net.minecraftforge.fml.common.network.simpleimpl.*;

public class HandlerNBTMessage implements IMessageHandler<MessageString, IMessage> {
    
    @Override
    public IMessage onMessage(MessageString message, MessageContext ctx) {
        ctx.getServerHandler().player.getHeldItemMainhand().setStackDisplayName(message.name);
        return null;
    }
}