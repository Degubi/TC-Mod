package degubi.teamcraft.network;

import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public final class MessageCoordInt implements IMessage {
    public int x;
    public int y;
    public int z;
    public int messageType;

    public MessageCoordInt(){}
    public MessageCoordInt(int posX, int posY, int posZ, int type){
        x = posX;
        y = posY;
        z = posZ;
        messageType = type;
    }
    
    @Override
    public void fromBytes(ByteBuf buf){
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        messageType = buf.readInt();
    }
    
    @Override
    public void toBytes(ByteBuf buf){
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(messageType);
    }
}