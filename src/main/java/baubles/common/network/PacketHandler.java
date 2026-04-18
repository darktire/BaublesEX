package baubles.common.network;

import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BaublesApi.MOD_ID);

    public static void init() {
        baubles.lib.network.PacketHandler.on(INSTANCE)
                .toSever(PacketOpen.class)
                .toSever(PacketSync.class)
                .toClient(PacketSync.class)
                .toClient(PacketModifier.class)
                .toClient(PacketFullSync.class)
                .toSever(PacketFakeTransaction.class);
    }
}