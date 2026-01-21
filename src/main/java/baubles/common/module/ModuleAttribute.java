package baubles.common.module;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import java.util.UUID;

public class ModuleAttribute extends AbstractModule {
    protected IAttribute attribute;
    protected float perLevel;
    protected int operation;

    public ModuleAttribute(UUID id, IAttribute attribute, float perLevel, int operation) {
        super(id);
        this.attribute = attribute;
        this.perLevel = perLevel;
        this.operation = operation;
    }

    @Override
    public void updateStatus(EntityLivingBase entity, int level) {
        IAttributeInstance instance = entity.getAttributeMap().getAttributeInstance(this.attribute);
        instance.removeModifier(this.id);
        if (level != 0) {
            instance.applyModifier(new AttributeModifier(this.id, this.id.toString(), getAmountIn(level), this.operation));
        }
    }

    protected float getAmountIn(int level) {
        return this.perLevel * level;
    }
}
