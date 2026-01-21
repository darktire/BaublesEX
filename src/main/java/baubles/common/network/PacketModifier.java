package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class PacketModifier implements IMessage {

    private int entityId;
    private int typeId;
    private int modifier;
    private int operation;
    private Collection<AttributeModifier> snapshots;

    public PacketModifier() {}

    public PacketModifier(EntityLivingBase entity, BaubleTypeEx type, int modifier, int operation) {
        this.entityId = entity.getEntityId();
        this.typeId = TypesData.getId(type);
        this.modifier = modifier;
        this.operation = operation;
    }

    public PacketModifier(EntityLivingBase entity, String typeName, int modifier, int operation) {
        this(entity, TypesData.getTypeByName(typeName), modifier, operation);
    }

    public PacketModifier(EntityLivingBase entity, BaubleTypeEx type, Collection<AttributeModifier> modifiers) {
        this.entityId = entity.getEntityId();
        this.typeId = TypesData.getId(type);
        this.snapshots = modifiers;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        buff.writeInt(entityId);
        buff.writeInt(typeId);
        if (snapshots == null) {
            buff.writeBoolean(true);
            buff.writeInt(modifier);
            buff.writeByte(operation);
        }
        else {
            buff.writeBoolean(false);
            buff.writeInt(snapshots.size());
            for (AttributeModifier attributemodifier : snapshots) {
                buff.writeUniqueId(attributemodifier.getID());
                buff.writeDouble(attributemodifier.getAmount());
                buff.writeByte(attributemodifier.getOperation());
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        PacketBuffer buff = new PacketBuffer(buffer);
        entityId = buff.readInt();
        typeId = buff.readInt();
        boolean flag = buff.readBoolean();
        if (flag) {
            modifier = buff.readInt();
            operation = buff.readByte();
        }
        else {
            int size = buff.readInt();
            snapshots = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                UUID uuid = buff.readUniqueId();
                snapshots.add(new AttributeModifier(uuid, "Unknown", buff.readDouble(), buff.readByte()));
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketModifier, IMessage> {
        @Override
        public IMessage onMessage(PacketModifier message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> execute(message));
            return null;
        }

        private void execute(PacketModifier message) {
            World world = Baubles.proxy.getClientWorld();
            if (world == null) return;
            Entity entity = world.getEntityByID(message.entityId);
            if (entity instanceof EntityLivingBase) {
                IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
                AdvancedInstance instance = AttributeManager.getInstance(((EntityLivingBase) entity).getAttributeMap(), TypesData.getTypeById(message.typeId));
                if (message.snapshots == null) {
                    instance.applyAnonymousModifier(message.operation, message.modifier);
                }
                else {
                    instance.removeAllModifiers();
                    message.snapshots.forEach(instance::applyModifier);
                }
                baubles.updateContainer();
            }
        }
    }
}