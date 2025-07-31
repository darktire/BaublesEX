package baubles.common.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.util.BaublesContent;
import baubles.common.Baubles;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketModifySlots implements IMessage {

    private int playerId;
    private int typeId;
    private int modifier;

    public PacketModifySlots() {}

    public PacketModifySlots(EntityPlayer entity, String typeName, int modifier) {
        this.playerId = entity.getEntityId();
        this.typeId = typeName.equals("reset") ? -1 : BaublesContent.getTypeByName(typeName).getId();
        this.modifier = modifier;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeInt(typeId);
        buffer.writeInt(modifier);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        playerId = buffer.readInt();
        typeId = buffer.readInt();
        modifier = buffer.readInt();
    }

    public static class Handler implements IMessageHandler<PacketModifySlots, IMessage> {
        @Override
        public IMessage onMessage(PacketModifySlots message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> execute(message));
            return null;
        }

        private void execute(PacketModifySlots message) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(message.playerId);
            if (entity instanceof EntityLivingBase) {
                IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                if (message.typeId == -1) {
                    baubles.clearModifier();
                }
                else {
                    baubles.modifySlots(BaublesContent.getTypeById(message.typeId).getTypeName(), message.modifier);
                }
            }
        }
    }
}