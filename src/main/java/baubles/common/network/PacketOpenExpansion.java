package baubles.common.network;

import baubles.common.container.ContainerExpansion;
import baubles.common.container.ExpansionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenExpansion implements IMessage {

    public PacketOpenExpansion() {}

    @Override
    public void toBytes(ByteBuf buffer) {}

    @Override
    public void fromBytes(ByteBuf buffer) {}


    public static class Handler implements IMessageHandler<PacketOpenExpansion, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenExpansion message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ((WorldServer) player.world).addScheduledTask(() -> {
                ExpansionManager instance = ExpansionManager.getInstance();
                instance.closeExpansion(player);
                instance.openExpansion(player, ContainerExpansion.create(player));
            });
            return null;
        }
    }
}
