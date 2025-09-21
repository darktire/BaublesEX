package baubles.common.network;

import baubles.Baubles;
import baubles.proxy.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenBaublesInventory implements IMessage {

    public PacketOpenBaublesInventory() {}

    @Override
    public void toBytes(ByteBuf buffer) {}

    @Override
    public void fromBytes(ByteBuf buffer) {}


    public static class Handler implements IMessageHandler<PacketOpenBaublesInventory, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenBaublesInventory message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ((WorldServer) player.world).addScheduledTask(() -> {
                player.closeContainer();
                player.openGui(Baubles.instance, CommonProxy.GUI, player.world, 0, 0, 0);
            });
            return null;
        }
    }
}
