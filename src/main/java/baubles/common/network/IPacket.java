package baubles.common.network;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IPacket extends IMessage {
    @Override
    default void fromBytes(ByteBuf buf) {
        try {
            read(new PacketBuffer(buf));
        } catch (Exception e) {
            BaublesApi.log.error("network error reading", e);
        }
    }

    @Override
    default void toBytes(ByteBuf buf) {
        try {
            write(new PacketBuffer(buf));
        } catch (Exception e) {
            BaublesApi.log.error("network error writing", e);
        }
    }

    default void write(PacketBuffer buf) throws Exception {}

    default void read(PacketBuffer buf) throws Exception {}

    IMessage handlePacket(MessageContext ctx);
}
