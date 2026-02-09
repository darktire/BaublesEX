package baubles.common.network;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.*;
import java.util.stream.Collectors;

public class PacketModifier implements IPacket {

    private int entityId;
    private int typeId;
    private double baseValue;
    private Collection<SimpleModifier> modifiers;

    public PacketModifier() {}

    public PacketModifier(EntityLivingBase entity, BaubleTypeEx type, int modifier, int operation) {
        this.entityId = entity.getEntityId();
        this.typeId = TypeData.getId(type);
        this.baseValue = -1;
        this.modifiers = Collections.singleton(new SimpleModifier(null, modifier, operation));
    }

    public PacketModifier(EntityLivingBase entity, String typeName, int modifier, int operation) {
        this(entity, TypeData.getTypeByName(typeName), modifier, operation);
    }

    public PacketModifier(EntityLivingBase entity, BaubleTypeEx type, double baseValue, Collection<AttributeModifier> modifiers) {
        this.entityId = entity.getEntityId();
        this.typeId = TypeData.getId(type);
        this.baseValue = baseValue;
        this.modifiers = modifiers.stream().map(SimpleModifier::new).collect(Collectors.toList());
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeInt(entityId);
        buf.writeInt(typeId);
        buf.writeDouble(baseValue);
        buf.writeInt(modifiers.size());
        for (SimpleModifier modifier : modifiers) {
            boolean hasId = modifier.hasId();
            buf.writeBoolean(hasId);
            if (hasId) buf.writeUniqueId(modifier.id);
            buf.writeDouble(modifier.amount);
            buf.writeByte(modifier.operation);
        }
    }

    @Override
    public void read(PacketBuffer buf) {
        entityId = buf.readInt();
        typeId = buf.readInt();
        baseValue = buf.readDouble();
        int size = buf.readInt();
        modifiers = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            modifiers.add(new SimpleModifier(buf.readBoolean() ? buf.readUniqueId() : null, buf.readDouble(), buf.readByte()));
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
            AdvancedInstance instance = AttributeManager.getInstance(((EntityLivingBase) entity).getAttributeMap(), TypeData.getTypeById(this.typeId));
            if (baseValue >= 0) instance.setBase(baseValue);
            instance.removeAllModifiers();
            Map<Boolean, List<SimpleModifier>> collected = this.modifiers.stream().collect(Collectors.partitioningBy(SimpleModifier::hasId));
            collected.get(true).stream().map(m -> m.build("Network")).forEach(instance::applyModifier);
            collected.get(false).forEach(m -> instance.applyAnonymousModifier(m.operation, m.amount));
            baubles.updateContainer();
        }
    }

    public static class SimpleModifier {
        private final UUID id;
        private final double amount;
        private final int operation;

        public SimpleModifier(AttributeModifier modifier) {
            this.id = modifier.getID();
            this.amount = modifier.getAmount();
            this.operation = modifier.getOperation();
        }

        public SimpleModifier(UUID id, double amount, int operation) {
            this.id = id;
            this.amount = amount;
            this.operation = operation;
        }

        public boolean hasId() {
            return this.id != null;
        }

        public AttributeModifier build(String name) {
            return new AttributeModifier(id, name, amount, operation);
        }
    }
}