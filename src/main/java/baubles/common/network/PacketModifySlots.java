package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.registries.TypesData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketModifySlots implements IMessage {

    private int entityId;
    private int typeId;
    private int modifier;
    private int addition;

    public PacketModifySlots() {}

    /**
     * @param entity sever player
     * @param typeName  bauble type
     * @param modifier value of modifier, be used when addition == 3
     * @param addition 0 means setting; 1 means modifying base on previous; 2 means modifying base on normal; 3 means reset
     */
    public PacketModifySlots(EntityLivingBase entity, String typeName, int modifier, int addition) {
        this.entityId = entity.getEntityId();
        BaubleTypeEx type = TypesData.getTypeByName(typeName);
        if (type == null) this.typeId = -1;
        else this.typeId = TypesData.getId(type);
        this.modifier = modifier;
        this.addition = addition;
    }

    public PacketModifySlots(EntityLivingBase entity) {
        this.entityId = entity.getEntityId();
        this.typeId = -1;
        this.modifier = 0;
        this.addition = 3;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeInt(typeId);
        buffer.writeInt(modifier);
        buffer.writeInt(addition);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        entityId = buffer.readInt();
        typeId = buffer.readInt();
        modifier = buffer.readInt();
        addition = buffer.readInt();
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
            Entity entity = world.getEntityByID(message.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesModifiable baublesCL = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                if (message.typeId > -1) {
                    if (message.addition == 0) baublesCL.modifySlot(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else if (message.addition == 1) baublesCL.modifySlotOA(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else if (message.addition == 2) baublesCL.setSlot(TypesData.getTypeById(message.typeId).getTypeName(), message.modifier);
                    else if (message.addition == 3) baublesCL.clearModifier();
                    baublesCL.updateContainer();
                }
            }
        }
    }
}