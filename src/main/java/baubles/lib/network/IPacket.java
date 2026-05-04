package baubles.lib.network;

import baubles.Reference;
import baubles.mixin.early.forge.AccessorItemStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public interface IPacket extends IMessage {
    @Override
    default void fromBytes(ByteBuf buf) {
        try {
            read(new PacketBuffer(buf));
        } catch (Exception e) {
            Reference.LOG.error("network error reading", e);
        }
    }

    @Override
    default void toBytes(ByteBuf buf) {
        try {
            write(new PacketBuffer(buf));
        } catch (Exception e) {
            Reference.LOG.error("network error writing", e);
        }
    }

    default void write(PacketBuffer buf) throws Exception {}

    default void read(PacketBuffer buf) throws Exception {}

    IPacket handlePacket(MessageContext ctx);

    /**
     * fix bug in vanilla writing forge tag
     */
    static PacketBuffer writeItemStack(PacketBuffer buf, ItemStack stack) {
        if (stack.isEmpty()) {
            buf.writeShort(-1);
        } else {
            buf.writeShort(Item.getIdFromItem(stack.getItem()));
            buf.writeByte(stack.getCount());
            buf.writeShort(stack.getMetadata());
            buf.writeCompoundTag(((AccessorItemStack) (Object) stack).capNBT());
            buf.writeCompoundTag(stack.getItem().isDamageable() || stack.getItem().getShareTag() ? stack.getItem().getNBTShareTag(stack) : null);
        }
        return buf;
    }

    /**
     * fix bug in vanilla reading forge tag
     */
    static ItemStack readItemStack(PacketBuffer buf) throws IOException {
        int id = buf.readShort();
        if (id < 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack = new ItemStack(Item.getItemById(id), buf.readByte(), buf.readShort(), buf.readCompoundTag());
            itemstack.getItem().readNBTShareTag(itemstack, buf.readCompoundTag());
            return itemstack;
        }
    }
}
