package baubles.api.attribute;

import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class AdvancedInstance extends ModifiableAttributeInstance {
    private WeakReference<IBaublesItemHandler> handler;
    private final Map<Integer, Double> anonymous = new HashMap<>();
    public boolean isModified = false;

    public AdvancedInstance(AbstractAttributeMap map, IAttribute attribute) {
        super(map, attribute);
        for (int i = 0; i < 3; i++) {
            this.anonymous.put(i, 0D);
        }
    }

    @Override
    public void setBaseValue(double ignore) {}

    @Override
    protected double computeValue() {
        double d0 = this.getBaseValue();

        d0 += this.getAppliedModifiers(0).stream()
                .mapToDouble(AttributeModifier::getAmount)
                .sum();
        d0 += this.anonymous.get(0);

        double d1 = d0;
        double finalD = d0;
        d1 += this.getAppliedModifiers(1).stream()
                .mapToDouble(attributemodifier -> finalD * attributemodifier.getAmount())
                .sum();
        d1 += d0 * this.anonymous.get(1);

        d1 = this.getAppliedModifiers(2).stream()
                .mapToDouble(attributemodifier -> 1.0D + attributemodifier.getAmount())
                .reduce(d1, (a, b) -> a * b);
        d1 *= 1.0D + this.anonymous.get(2);

        return this.genericAttribute.clampValue(d1);
    }

    /**
     * needsUpdate -> cacheValue
     * isModified -> sync
     */
    @Override
    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.isModified = true;
        IBaublesItemHandler baubles = this.handler.get();
        if (baubles != null) {
            ((BaublesContainer) baubles).containerUpdated = false;
        }
    }

    public void setBase(double value) {
        super.setBaseValue(value);
    }

    public double getAnonymousModifier(int operation) {
        return this.anonymous.get(operation);
    }

    public void applyAnonymousModifier(int operation, double modifier) {
        this.anonymous.put(operation, modifier);
        this.flagForUpdate();
    }

    public void setListener(IBaublesItemHandler handler) {
        this.handler = new WeakReference<>(handler);
    }
}
