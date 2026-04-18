package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.lib.network.IPacket;
import com.github.bsideup.jabel.Desugar;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class PacketSync implements IPacket {

    private boolean toClient;
    private int entityId;
    private final List<Entry> entries = new ArrayList<>();

    public PacketSync() {}

    private PacketSync(EntityLivingBase entity) {
        this.toClient = entity != null;
        this.entityId = this.toClient ? entity.getEntityId() : -1;
    }

    @Desugar
    record Entry(int slot, ItemStack stack, int visible) {}

    public static PacketSync S2CPack(EntityLivingBase entity) {
        return new PacketSync(entity);
    }

    public static PacketSync S2CPack(EntityLivingBase entity, int slot, ItemStack stack, int visible) {
        return new PacketSync(entity).append(slot, stack, visible);
    }

    public static PacketSync C2SPack(int slot, ItemStack stack, int visible) {
        return new PacketSync(null).append(slot, stack, visible);
    }

    public PacketSync append(int slot, ItemStack stack, int visible) {
        entries.add(new Entry(slot, stack, visible));
        return this;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeBoolean(this.toClient);
        if (this.toClient) {
            buf.writeInt(this.entityId);
        }
        buf.writeInt(entries.size());
        for (Entry e : entries) {
            buf.writeInt(e.slot);
            buf.writeBoolean(e.stack != null);
            if (e.stack != null) buf.writeItemStack(e.stack);
            buf.writeInt(e.visible);
        }
    }

    @Override
    public void read(PacketBuffer buf) throws Exception {
        this.toClient = buf.readBoolean();
        if (this.toClient) {
            this.entityId = buf.readInt();
        }
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            int slot = buf.readInt();
            boolean hasStack = buf.readBoolean();
            ItemStack stack = hasStack ? buf.readItemStack() : null;
            int visible = buf.readInt();
            entries.add(new Entry(slot, stack, visible));
        }
    }

    @Override
    public IPacket handlePacket(MessageContext ctx) {
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
        for (Entry e : entries) {
            if (e.stack != null) baubles.setStackInSlot(e.slot, e.stack);
            if (e.visible != -1) baubles.setVisible(e.slot, e.visible == 1);
        }
    }

    private void handleClient() {
        World world = Baubles.proxy.getClientWorld();
        if (world != null) {
            Entity entity = world.getEntityByID(this.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                baubles.updateContainer();
                for (Entry e : entries) {
                    if (e.stack != null) baubles.setStackInSlot(e.slot, e.stack);
                    if (e.visible != -1) baubles.setVisible(e.slot, e.visible == 1);
                }
            }
        }
    }
}