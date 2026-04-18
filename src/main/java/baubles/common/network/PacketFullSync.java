package baubles.common.network;

import baubles.lib.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class PacketFullSync implements IPacket {

    private final List<PacketModifier> modifiers = new ArrayList<>();
    private PacketSync baubles;

    public void addModifier(PacketModifier modifier) {
        this.modifiers.add(modifier);
    }

    public void setBaubles(PacketSync baubles) {
        this.baubles = baubles;
    }

    @Override
    public void write(PacketBuffer buf) throws Exception {
        buf.writeInt(modifiers.size());
        for (PacketModifier modifier : modifiers) {
            modifier.write(buf);
        }
        buf.writeBoolean(baubles != null);
        if (baubles != null) baubles.write(buf);
    }

    @Override
    public void read(PacketBuffer buf) throws Exception {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            PacketModifier pkt = new PacketModifier();
            pkt.read(buf);
            modifiers.add(pkt);
        }
        if (buf.readBoolean()) {
            baubles = new PacketSync();
            baubles.read(buf);
        }
    }

    @Override
    public IPacket handlePacket(MessageContext ctx) {
        modifiers.forEach(m -> m.handlePacket(ctx));
        if (baubles != null) baubles.handlePacket(ctx);
        return null;
    }
}
