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

    private int id;
    private int entityId;
    private int slot;
    private ItemStack stack;
    private boolean visible;

    public PacketSync() {}

    public PacketSync(EntityPlayer p, int slot, ItemStack bauble) {}

    public PacketSync(EntityLivingBase entity, int slot, ItemStack stack, boolean visible) {
        this.id = 0;
        this.entityId = entity.getEntityId();
        this.slot = slot;
        this.stack = stack;
        this.visible = visible;
    }

    public PacketSync(EntityLivingBase entity, int slot, boolean visible) {
        this.id = 1;
        this.entityId = entity.getEntityId();
        this.slot = slot;
        this.visible = visible;
    }

    public PacketSync(int slot, boolean visible) {
        this.id = -1;
        this.slot = slot;
        this.visible = visible;
    }


    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.id);
        if (this.id == -1) {
            buffer.writeInt(this.slot);
            buffer.writeBoolean(this.visible);
        }
        else if (this.id == 0) {
            buffer.writeInt(this.entityId);
            buffer.writeInt(this.slot);
            ByteBufUtils.writeItemStack(buffer, this.stack);
            buffer.writeBoolean(this.visible);
        }
        else if (this.id == 1) {
            buffer.writeInt(this.entityId);
            buffer.writeInt(this.slot);
            buffer.writeBoolean(this.visible);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        this.id = buffer.readInt();
        if (this.id == -1) {
            this.slot = buffer.readInt();
            this.visible = buffer.readBoolean();
        }
        else if (this.id == 0) {
            this.entityId = buffer.readInt();
            this.slot = buffer.readInt();
            this.stack = ByteBufUtils.readItemStack(buffer);
            this.visible = buffer.readBoolean();
        }
        else if (this.id == 1) {
            this.entityId = buffer.readInt();
            this.slot = buffer.readInt();
            this.visible = buffer.readBoolean();
        }
    }

    public static class Handler implements IMessageHandler<PacketSync, IMessage> {
        @Override
        public IMessage onMessage(PacketSync msg, MessageContext ctx) {
            if (ctx.side == Side.CLIENT && msg.id >= 0) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    World world = Baubles.proxy.getClientWorld();
                    if (world == null) return;
                    Entity entity = world.getEntityByID(msg.entityId);
                    if (entity instanceof EntityLivingBase) {
                        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                        int i = msg.slot;
                        if (msg.id == 0) baubles.setStackInSlot(i, msg.stack);
                        baubles.setVisible(i, msg.visible);
                    }
                });
            }
            else if (ctx.side == Side.SERVER && msg.id < 0) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ((WorldServer) player.world).addScheduledTask(() -> {
                    IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
                    baubles.setVisible(msg.slot, msg.visible);
                });
            }
            return null;
        }
    }
}