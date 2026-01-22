package baubles.common.network;

import baubles.Baubles;
import baubles.common.container.ContainerExpansion;
import baubles.common.container.ExpansionManager;
import baubles.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpen implements IPacket {

    private Option option;

    public PacketOpen() {}

    public PacketOpen(Option option) {
        this.option = option;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeEnumValue(this.option);
    }

    @Override
    public void read(PacketBuffer buf) {
        this.option = buf.readEnumValue(Option.class);
    }

    @Override
    public IMessage handlePacket(MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        ((WorldServer) player.world).addScheduledTask(() -> this.option.apply(player));
        return null;
    }

    public enum Option {
        NORMAL {
            @Override
            public void apply(EntityPlayerMP player) {
                player.closeContainer();
                player.openContainer = player.inventoryContainer;
            }
        },

        EXPANSION {
            @Override
            public void apply(EntityPlayerMP player) {
                player.closeContainer();
                player.openGui(Baubles.instance, CommonProxy.GUI, player.world, 0, 0, 0);
            }
        },


        ENHANCEMENT {
            @Override
            public void apply(EntityPlayerMP player) {
                ExpansionManager instance = ExpansionManager.getInstance();
                instance.closeExpansion(player);
                instance.openExpansion(player, new ContainerExpansion(player));
            }
        };

        abstract void apply(EntityPlayerMP player);
    }
}
