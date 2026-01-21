package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
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
    private boolean hasStack;
    private ItemStack stack;
    private int visible;

    public PacketSync() {}

    @SuppressWarnings("unused")
    public PacketSync(EntityPlayer p, int slot, ItemStack bauble) {}

    public static PacketSync S2CPack(EntityLivingBase entity, int slot, ItemStack stack, int visible) {
        PacketSync pkt = PacketPool.borrow(entity, slot, stack, visible);
        pkt.toClient = true;
        return pkt;
    }

    public static PacketSync C2SPack(int slot, ItemStack stack, int visible) {
        PacketSync pkt = PacketPool.borrow(null, slot, stack, visible);
        pkt.toClient = false;
        return pkt;
    }

    PacketSync set(int entityId, int slot, ItemStack stack, int visible) {
        this.entityId = entityId;
        this.slot = slot;
        this.visible = visible;
        this.hasStack = stack != null;
        this.stack = stack;
        return this;
    }

    void reset() {
        this.entityId = -1;
        this.slot = -1;
        this.hasStack = false;
        this.stack = null;
        this.visible = -1;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(this.toClient);
        if (this.toClient) {
            buffer.writeInt(this.entityId);
        }
        buffer.writeBoolean(this.hasStack);
        if (this.hasStack) {
            ByteBufUtils.writeItemStack(buffer, this.stack);
        }
        buffer.writeInt(this.slot);
        buffer.writeInt(this.visible);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.toClient = buffer.readBoolean();
        if (this.toClient) {
            this.entityId = buffer.readInt();
        }
        this.hasStack = buffer.readBoolean();
        if (this.hasStack) {
            this.stack = ByteBufUtils.readItemStack(buffer);
        }
        this.slot = buffer.readInt();
        this.visible = buffer.readInt();
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
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
            if (msg.hasStack) baubles.setStackInSlot(msg.slot, msg.stack);
            baubles.setVisible(msg.slot, msg.visible == 1);
            PacketPool.release(msg);
        }

        private void handleClient(PacketSync msg) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(msg.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                if (msg.hasStack) baubles.setStackInSlot(msg.slot, msg.stack);
                if (msg.visible != -1) baubles.setVisible(msg.slot, msg.visible == 1);
            }
            PacketPool.release(msg);
        }
    }
}