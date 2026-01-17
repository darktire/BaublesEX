package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
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
    private int operation;

    public PacketModifySlots() {}

    public PacketModifySlots(EntityLivingBase entity, String typeName, int modifier, int operation) {
        this.entityId = entity.getEntityId();
        try {
            BaubleTypeEx type = TypesData.getTypeByName(typeName);
            this.typeId = TypesData.getId(type);
        } catch (Throwable e) {
            this.typeId = -1;
        }
        this.modifier = modifier;
        this.operation = operation;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeInt(typeId);
        buffer.writeInt(modifier);
        buffer.writeInt(operation);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        entityId = buffer.readInt();
        typeId = buffer.readInt();
        modifier = buffer.readInt();
        operation = buffer.readInt();
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
                IBaublesItemHandler baublesCL = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                AttributeManager.getInstance(((EntityLivingBase) entity).getAttributeMap(), TypesData.getTypeById(message.typeId)).applyAnonymousModifier(message.operation, message.modifier);
                baublesCL.updateContainer();
            }
        }
    }
}