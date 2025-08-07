package baubles.common.network;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.util.TypesData;
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
    private boolean addition;

    public PacketModifySlots() {
    }

    public PacketModifySlots(EntityPlayer entity, String typeName, int modifier, boolean addition) {
        this.playerId = entity.getEntityId();
        if (typeName.equals("reset")) this.typeId = -2;
        else {
            BaubleTypeEx type = TypesData.getTypeByName(typeName);
            if (type == null) this.typeId = -1;
            else this.typeId = TypesData.getId(type);
        }
        this.modifier = modifier;
        this.addition = addition;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(playerId);
        buffer.writeInt(typeId);
        buffer.writeInt(modifier);
        buffer.writeBoolean(addition);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        playerId = buffer.readInt();
        typeId = buffer.readInt();
        modifier = buffer.readInt();
        addition = buffer.readBoolean();
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
                if (message.typeId == -2) {
                    baubles.clearModifier();
                }
                else if (message.typeId > -1) {
                    if (message.addition) baubles.modifySlotOA(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else baubles.modifySlot(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                }
            }
        }
    }
}