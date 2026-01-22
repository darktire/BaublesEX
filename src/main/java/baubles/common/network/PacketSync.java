package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSync implements IPacket {

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
    public void write(PacketBuffer buf) {
        buf.writeBoolean(this.toClient);
        if (this.toClient) {
            buf.writeInt(this.entityId);
        }
        buf.writeBoolean(this.hasStack);
        if (this.hasStack) {
            buf.writeItemStack(stack);
        }
        buf.writeInt(this.slot);
        buf.writeInt(this.visible);
    }

    @Override
    public void read(PacketBuffer buf) throws Exception {
        this.toClient = buf.readBoolean();
        if (this.toClient) {
            this.entityId = buf.readInt();
        }
        this.hasStack = buf.readBoolean();
        if (this.hasStack) {
            this.stack = buf.readItemStack();
        }
        this.slot = buf.readInt();
        this.visible = buf.readInt();
    }

    @Override
    public IMessage handlePacket(MessageContext ctx) {
        if (ctx.side == Side.CLIENT && this.toClient) {
            Minecraft.getMinecraft().addScheduledTask(this::handleClient);
        }
        else if (ctx.side == Side.SERVER && !this.toClient) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ((WorldServer) player.world).addScheduledTask(() -> handleSever(player));
        }
        return null;
    }

    private void handleSever(EntityLivingBase player) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
        if (this.hasStack) baubles.setStackInSlot(this.slot, this.stack);
        baubles.setVisible(this.slot, this.visible == 1);
        PacketPool.release(this);
    }

    private void handleClient() {
        World world = Baubles.proxy.getClientWorld();
        if (world == null) return;
        Entity entity = world.getEntityByID(this.entityId);
        if (entity instanceof EntityLivingBase) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
            if (this.hasStack) baubles.setStackInSlot(this.slot, this.stack);
            if (this.visible != -1) baubles.setVisible(this.slot, this.visible == 1);
        }
        PacketPool.release(this);
    }
}