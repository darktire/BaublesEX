package baubles.common.network;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketIncrOffset implements IMessage {

    private int value;

    public PacketIncrOffset() {}

    public PacketIncrOffset(int value) {
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        value = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(value);
    }

    public static class Handler implements IMessageHandler<PacketIncrOffset, IMessage> {
        @Override
        public IMessage onMessage(PacketIncrOffset message, MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
            mainThread.addScheduledTask(() -> {
                BaublesItemHandler baubles = (BaublesItemHandler) ctx.getServerHandler().player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);;
                if (baubles != null) {
                    baubles.incrOffset(message.value);
                }
            });
            return null;
        }
    }
}
