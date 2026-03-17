package baubles.common.network;

import baubles.api.BaublesApi;
import baubles.lib.network.PacketHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(BaublesApi.MOD_ID);

    public static void init() {
        PacketHandler.on(CHANNEL)
                .toSever(PacketOpen.class)
                .toSever(PacketSync.class)
                .toClient(PacketSync.class)
                .toClient(PacketModifier.class)
                .toSever(PacketFakeTransaction.class);
    }
}