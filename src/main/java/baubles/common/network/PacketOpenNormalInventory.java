package baubles.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenNormalInventory implements IMessage {

    public PacketOpenNormalInventory() {}

    @Override
    public void toBytes(ByteBuf buffer) {}

    @Override
    public void fromBytes(ByteBuf buffer) {}


    public static class Handler implements IMessageHandler<PacketOpenNormalInventory, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenNormalInventory message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ((WorldServer) player.world).addScheduledTask(() -> {
                player.closeContainer();
                player.openContainer = player.inventoryContainer;
            });
            return null;
        }
    }
}
