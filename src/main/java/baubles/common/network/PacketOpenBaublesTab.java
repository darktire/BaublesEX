package baubles.common.network;

import baubles.common.Baubles;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenBaublesTab implements IMessage, IMessageHandler<PacketOpenBaublesTab, IMessage> {

    public PacketOpenBaublesTab() {}

    @Override
    public IMessage onMessage(PacketOpenBaublesTab message, MessageContext ctx) {
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
        mainThread.addScheduledTask(() -> {
            ctx.getServerHandler().player.openContainer.onContainerClosed(ctx.getServerHandler().player);
            ctx.getServerHandler().player.openGui(Baubles.instance, Baubles.TAB, ctx.getServerHandler().player.world, 0, 0, 0);
        });
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {}
    @Override
    public void toBytes(ByteBuf buf) {}
}
