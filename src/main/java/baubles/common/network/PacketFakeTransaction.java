package baubles.common.network;

import baubles.common.container.ContainerExpansion;
import baubles.common.container.ExpansionManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFakeTransaction implements IMessage {
    private int slot;
    private int button;
    private ClickType type;
    private ItemStack stack;

    public PacketFakeTransaction() {}

    private PacketFakeTransaction(int slot, int button, ClickType type, ItemStack stack) {
        this.slot = slot;
        this.button = button;
        this.type = type;
        this.stack = stack;
    }

    public static PacketFakeTransaction C2SPack(int slotId, int mouseButton, ClickType type, ItemStack itemstack) {
        return new PacketFakeTransaction(slotId, mouseButton, type, itemstack);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(this.slot);
        buffer.writeInt(this.button);
        buffer.writeEnumValue(this.type);
        buffer.writeItemStack(this.stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.slot = buffer.readInt();
        this.button = buffer.readInt();
        this.type = buffer.readEnumValue(ClickType.class);
        try {
            this.stack = buffer.readItemStack();
        } catch (Throwable ignored) {}

    }

    public static class Handler implements IMessageHandler<PacketFakeTransaction, IMessage> {
        @Override
        public IMessage onMessage(PacketFakeTransaction msg, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ExpansionManager manager = ExpansionManager.getInstance();

                ContainerExpansion expansion = manager.getExpansion(player);
                if (!expansion.canInteractWith(player)) return;

                expansion.slotClick(msg.slot, msg.button, msg.type, player);
            });
            return null;
        }
    }
}
