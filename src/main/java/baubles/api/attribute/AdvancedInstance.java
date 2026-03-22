package baubles.api.attribute;

import baubles.api.cap.BaublesContainer;
import baubles.lib.util.AttrOpt;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.lang.ref.WeakReference;
import java.util.Collection;

public class AdvancedInstance extends ModifiableAttributeInstance {
    private WeakReference<BaublesContainer> handler;
    private final double[] anonymous = new double[3];
    boolean needsUpdate = false;
    protected int flags = 0;
    protected final double[] modifierCache = new double[3];

    public AdvancedInstance(AbstractAttributeMap map, IAttribute attribute) {
        super(map, attribute);
        for (int i = 0; i < 3; i++) {
            this.anonymous[i] = 0;
            this.markDirty(AttrOpt.values()[i]);
        }
    }

    @Override
    public void setBaseValue(double ignore) {}

    @Override
    public void applyModifier(AttributeModifier modifier) {
        super.applyModifier(modifier);
        this.markDirty(AttrOpt.getOpt(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        super.removeModifier(modifier);
        this.markDirty(AttrOpt.getOpt(modifier));
    }

    @Override
    protected void flagForUpdate() {}

    @Override
    public double getAttributeValue() {
        if (this.needsUpdate) {
            this.cachedValue = this.computeValue(getBaseValue());
            this.needsUpdate = false;
        }

        return this.cachedValue;
    }

    public double computeValue(double base) {
        base += getCachedModifierValue(AttrOpt.ADDITION);
        base *= (1 + getCachedModifierValue(AttrOpt.MULTIPLY_BASE));
        base *= getCachedModifierValue(AttrOpt.MULTIPLY_TOTAL);

        return this.genericAttribute.clampValue(base);
    }

    public void markDirty(AttrOpt type) {
        this.flags |= type.getMask();
        this.needsUpdate = true;
    }

    public boolean isDirty(AttrOpt type) {
        return (this.flags & type.getMask()) != 0;
    }

    protected double getCachedModifierValue(AttrOpt type) {
        int typeId = type.get();

        if (!isDirty(type)) return this.modifierCache[typeId];

        Collection<AttributeModifier> modifiers = this.getAppliedModifiers(typeId);
        double result = switch (typeId) {
            case 0, 1 -> modifiers.stream()
                    .mapToDouble(AttributeModifier::getAmount)
                    .sum();
            case 2 -> modifiers.stream()
                    .mapToDouble(m -> 1.0D + m.getAmount())
                    .reduce(1.0D, (a, b) -> a * b);
            default -> 0.0D;
        };

        result += anonymous[typeId];
        this.modifierCache[typeId] = result;
        this.flags &= ~type.getMask();
        return result;
    }

    public void setBase(double value) {
        super.setBaseValue(value);
    }

    public double getAnonymousModifier(int operation) {
        return this.anonymous[operation];
    }

    public void applyAnonymousModifier(int operation, double modifier) {
        if (this.anonymous[operation] == modifier) return;
        this.anonymous[operation] = modifier;
        this.markDirty(AttrOpt.values()[operation]);
    }

    public void setListener(BaublesContainer handler) {
        this.handler = new WeakReference<>(handler);
    }

    public void callback() {
        BaublesContainer baubles = this.handler.get();
        if (baubles != null) {
            baubles.containerUpdated = false;
        }
    }
}
