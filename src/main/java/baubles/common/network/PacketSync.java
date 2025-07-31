package baubles.common.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.Baubles;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class PacketSync implements IMessage {

    private int playerId;
    private int slot = 0;
    private ItemStack bauble;

    public PacketSync() {}

    public PacketSync(EntityPlayer p, int slot, ItemStack bauble) {
        this.slot = slot;
        this.bauble = bauble;
        this.playerId = p.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeInt(slot);
        writeItemStack(buffer, bauble);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        playerId = buffer.readInt();
        slot = buffer.readInt();
        bauble = readItemStack(buffer);
    }

    public void writeItemStack(ByteBuf to, ItemStack stack) {
        new PacketBuffer(to).writeCompoundTag(stack.serializeNBT());
    }

    public ItemStack readItemStack(ByteBuf to) {
        PacketBuffer buffer = new PacketBuffer(to);
        try {
            NBTTagCompound compound = buffer.readCompoundTag();
            if (compound != null) return new ItemStack(compound);
            return buffer.readItemStack();
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public static class Handler implements IMessageHandler<PacketSync, IMessage> {
        @Override
        public IMessage onMessage(PacketSync message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> execute(message));
            return null;
        }

        private void execute(PacketSync message) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(message.playerId);
            if (entity instanceof EntityLivingBase) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.setStackInSlot(message.slot, message.bauble);
            }
        }
    }
}