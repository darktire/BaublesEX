package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class PacketModifier implements IPacket {

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
    public void write(PacketBuffer buf) {
        buf.writeInt(entityId);
        buf.writeInt(typeId);
        if (snapshots == null) {
            buf.writeBoolean(true);
            buf.writeInt(modifier);
            buf.writeByte(operation);
        }
        else {
            buf.writeBoolean(false);
            buf.writeInt(snapshots.size());
            for (AttributeModifier attributemodifier : snapshots) {
                buf.writeUniqueId(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }

    @Override
    public void read(PacketBuffer buf) {
        entityId = buf.readInt();
        typeId = buf.readInt();
        boolean flag = buf.readBoolean();
        if (flag) {
            modifier = buf.readInt();
            operation = buf.readByte();
        }
        else {
            int size = buf.readInt();
            snapshots = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                UUID uuid = buf.readUniqueId();
                snapshots.add(new AttributeModifier(uuid, "Unknown", buf.readDouble(), buf.readByte()));
            }
        }
    }

    @Override
    public IMessage handlePacket(MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(this::execute);
        return null;
    }

    private void execute() {
        World world = Baubles.proxy.getClientWorld();
        if (world == null) return;
        Entity entity = world.getEntityByID(this.entityId);
        if (entity instanceof EntityLivingBase) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) entity);
            AdvancedInstance instance = AttributeManager.getInstance(((EntityLivingBase) entity).getAttributeMap(), TypesData.getTypeById(this.typeId));
            if (this.snapshots == null) {
                instance.applyAnonymousModifier(this.operation, this.modifier);
            }
            else {
                instance.removeAllModifiers();
                this.snapshots.forEach(instance::applyModifier);
            }
            baubles.updateContainer();
        }
    }
}