package baubles.common.module;

import com.google.common.base.Suppliers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class ModuleAttribute extends AbstractModule {
    protected Supplier<IAttribute> supplier;
    protected float perLevel;
    protected int operation;
    protected static Map<String, IAttribute> ATTRIBUTES = new HashMap<>();

    public ModuleAttribute(UUID id, Supplier<IAttribute> supplier, float perLevel, int operation) {
        super(id);
        this.supplier = supplier;
        this.perLevel = perLevel;
        this.operation = operation;
    }

    public static ModuleAttribute ofName(UUID id, String attrName, float perLevel, int operation) {
        return new NameBased(id, attrName, perLevel, operation);
    }

    @Override
    public void updateStatus(EntityLivingBase entity, int level) {
        IAttributeInstance instance = entity.getAttributeMap().getAttributeInstance(this.supplier.get());
        instance.removeModifier(this.id);
        if (level != 0) {
            instance.applyModifier(new AttributeModifier(this.id, this.id.toString(), getAmountIn(level), this.operation));
        }
    }

    protected float getAmountIn(int level) {
        return this.perLevel * level;
    }

    private static class NameBased extends ModuleAttribute {
        private final String name;

        public NameBased(UUID id, String name, float perLevel, int operation) {
            super(id, null, perLevel, operation);
            this.name = name;
        }

        @Override
        public void updateStatus(EntityLivingBase entity, int level) {
            IAttribute attribute = getAttributeFromName(entity);
            if (attribute != null) {
                this.supplier = Suppliers.memoize(() -> attribute);
                super.updateStatus(entity, level);
            }
        }

        private IAttribute getAttributeFromName(EntityLivingBase entity) {
            return ATTRIBUTES.computeIfAbsent(this.name, name -> {
                IAttributeInstance instance = entity.getAttributeMap().getAttributeInstanceByName(name);
                return instance == null ? null : instance.getAttribute();
            });
        }
    }
}
