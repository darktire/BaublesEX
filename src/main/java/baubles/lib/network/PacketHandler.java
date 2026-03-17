package baubles.lib.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler implements IMessageHandler<IPacket, IPacket> {

    protected PacketHandler() {}

    public static Builder on(SimpleNetworkWrapper instance) {
        return new Builder(instance);
    }

    @Override
    public IPacket onMessage(IPacket message, MessageContext ctx) {
        return message.handlePacket(ctx);
    }

    public static class Builder {
        private int id = 0;
        private final PacketHandler handler;
        private final SimpleNetworkWrapper network;

        protected Builder(SimpleNetworkWrapper network) {
            this.handler = new PacketHandler();
            this.network = network;
        }

        public Builder add(Class<? extends IPacket> cls, Side side) {
            this.register(cls, side);
            return this;
        }

        public Builder toClient(Class<? extends IPacket> cls) {
            this.register(cls, Side.CLIENT);
            return this;
        }

        public Builder toSever(Class<? extends IPacket> cls) {
            this.register(cls, Side.SERVER);
            return this;
        }

        public PacketHandler get() {
            return this.handler;
        }

        public SimpleNetworkWrapper build() {
            return this.network;
        }

        private void register(Class<? extends IPacket> cls, Side side) {
            this.network.registerMessage(this.handler, cls, this.id++, side);
        }
    }
}
