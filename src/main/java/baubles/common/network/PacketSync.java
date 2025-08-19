package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class PacketSync implements IMessage {

    private int entityId;
    private int slot = 0;
    private ItemStack bauble;

    public PacketSync() {}

    public PacketSync(EntityPlayer p, int slot, ItemStack bauble) {
        this((EntityLivingBase) p, slot, bauble);
    }

    public PacketSync(EntityLivingBase entity, int slot, ItemStack bauble) {
        this.slot = slot;
        this.bauble = bauble;
        this.entityId = entity.getEntityId();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeInt(slot);
        writeItemStack(buffer, bauble);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        entityId = buffer.readInt();
        slot = buffer.readInt();
        bauble = readItemStack(buffer);
    }

    public void writeItemStack(ByteBuf to, ItemStack stack) {
        new PacketBuffer(to).writeItemStack(stack);
    }

    public ItemStack readItemStack(ByteBuf to) {
        PacketBuffer buffer = new PacketBuffer(to);
        try { return buffer.readItemStack(); }
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
            Entity entity = world.getEntityByID(message.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.setStackInSlot(message.slot, message.bauble);
            }
        }
    }
}