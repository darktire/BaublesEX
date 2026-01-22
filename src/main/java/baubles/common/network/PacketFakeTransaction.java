package baubles.common.network;

import baubles.common.container.ContainerExpansion;
import baubles.common.container.ExpansionManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFakeTransaction implements IPacket {
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
    public IMessage handlePacket(MessageContext ctx) {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ExpansionManager manager = ExpansionManager.getInstance();

            ContainerExpansion expansion = manager.getExpansion(player);
            if (!expansion.canInteractWith(player)) return;

            expansion.slotClick(this.slot, this.button, this.type, player);
        });
        return null;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeInt(this.slot);
        buf.writeInt(this.button);
        buf.writeEnumValue(this.type);
        buf.writeItemStack(this.stack);
    }

    @Override
    public void read(PacketBuffer buf) throws Exception {
        this.slot = buf.readInt();
        this.button = buf.readInt();
        this.type = buf.readEnumValue(ClickType.class);
        this.stack = buf.readItemStack();
    }
}
