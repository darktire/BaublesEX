package baubles.common.network;

import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class PacketHandler implements IMessageHandler<IPacket, IMessage> {
    private static final PacketHandler HANDLER = new PacketHandler();
    private static int START_ID = 0;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BaublesApi.MOD_ID);
    private static final ImmutableList<ImmutablePair<Class<? extends IPacket>, Side>> PACKETS = ImmutableList.of(
            ImmutablePair.of(PacketOpen.class, Side.SERVER),
            ImmutablePair.of(PacketSync.class, Side.CLIENT),
            ImmutablePair.of(PacketSync.class, Side.SERVER),
            ImmutablePair.of(PacketModifier.class, Side.CLIENT),
            ImmutablePair.of(PacketFakeTransaction.class, Side.SERVER)
    );

    public static void init() {
        PACKETS.forEach(PacketHandler::register);
    }

    private static void register(ImmutablePair<Class<? extends IPacket>, Side> pair) {
        INSTANCE.registerMessage(HANDLER, pair.getLeft(), START_ID++, pair.getRight());
    }

    @Override
    public IMessage onMessage(IPacket message, MessageContext ctx) {
        return message.handlePacket(ctx);
    }
}