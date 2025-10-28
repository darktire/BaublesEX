package baubles.common.network;

import baubles.api.BaublesApi;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static int START_ID = 0;
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BaublesApi.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(PacketOpenBaublesInventory.Handler.class, PacketOpenBaublesInventory.class, START_ID++, Side.SERVER);
        INSTANCE.registerMessage(PacketOpenNormalInventory.Handler.class, PacketOpenNormalInventory.class, START_ID++, Side.SERVER);
        INSTANCE.registerMessage(PacketSync.Handler.class, PacketSync.class, START_ID++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSync.Handler.class, PacketSync.class, START_ID++, Side.SERVER);
        INSTANCE.registerMessage(PacketModifySlots.Handler.class, PacketModifySlots.class, START_ID++, Side.CLIENT);
        INSTANCE.registerMessage(PacketOpenExpansion.Handler.class, PacketOpenExpansion.class, START_ID++, Side.SERVER);
    }
}