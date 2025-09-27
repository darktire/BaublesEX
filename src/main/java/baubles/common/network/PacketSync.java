package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSync implements IMessage {

    private boolean toClient;
    private int entityId;
    private int slot;
    private ItemStack stack;
    private boolean visible;

    public PacketSync() {}

    @SuppressWarnings("unused")
    public PacketSync(EntityPlayer p, int slot, ItemStack bauble) {}

    public static PacketSync severPack(EntityLivingBase entity, int slot, ItemStack stack, boolean visible) {
        PacketSync pkt = PacketPool.borrow(entity, slot, stack, visible);
        pkt.toClient = true;
        return pkt;
    }

    public static PacketSync clientPack(int slot, boolean visible) {
        PacketSync pkt = PacketPool.borrow(null, slot, null, visible);
        pkt.toClient = false;
        return pkt;
    }

    PacketSync set(int entityId, int slot, ItemStack stack, boolean visible) {
        this.entityId = entityId;
        this.slot = slot;
        this.visible = visible;
        this.stack = stack;
        return this;
    }

    void reset() {
        this.entityId = -1;
        this.slot = -1;
        this.stack = ItemStack.EMPTY;
        this.visible = false;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(this.toClient);
        if (this.toClient) {
            buffer.writeInt(this.entityId);
            ByteBufUtils.writeItemStack(buffer, this.stack);
        }
        buffer.writeInt(this.slot);
        buffer.writeBoolean(this.visible);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.toClient = buffer.readBoolean();
        if (this.toClient) {
            this.entityId = buffer.readInt();
            this.stack = ByteBufUtils.readItemStack(buffer);
        }
        this.slot = buffer.readInt();
        this.visible = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketSync, IMessage> {
        @Override
        public IMessage onMessage(PacketSync msg, MessageContext ctx) {
            if (ctx.side == Side.CLIENT && msg.toClient) {
                Minecraft.getMinecraft().addScheduledTask(() -> handleClient(msg));
            }
            else if (ctx.side == Side.SERVER && !msg.toClient) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ((WorldServer) player.world).addScheduledTask(() -> handleSever(msg, player));
            }
            return null;
        }

        private void handleSever(PacketSync msg, EntityLivingBase player) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler(player);
            baubles.setVisible(msg.slot, msg.visible);
            baubles.markDirty(msg.slot);
            PacketPool.release(msg);
        }

        private void handleClient(PacketSync msg) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(msg.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.setStackInSlot(msg.slot, msg.stack);
                baubles.setVisible(msg.slot, msg.visible);
            }
            PacketPool.release(msg);
        }
    }
}