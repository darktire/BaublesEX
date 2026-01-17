package baubles.api.attribute;

import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.ai.attributes.*;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.DoubleBinaryOperator;

public class AdvancedInstance extends ModifiableAttributeInstance {
    public static final DoubleBinaryOperator MULTIPLICATION = (a, b) -> a * b;
    private WeakReference<IBaublesItemHandler> handler;
    private final Map<Integer, Double> anonymous = new HashMap<>();

    public AdvancedInstance(AbstractAttributeMap map, IAttribute attribute) {
        super(map, attribute);
        for (int i = 0; i < 3; i++) {
            this.anonymous.put(i, 0D);
        }
    }

    @Override
    public void setBaseValue(double baseValue) {
        super.setBaseValue(baseValue);
        this.inform();
    }

    @Override
    public void applyModifier(AttributeModifier modifier) {
        super.applyModifier(modifier);
        this.inform();
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        super.removeModifier(modifier);
        this.inform();
    }

    @Override
    public void removeModifier(UUID p_188479_1_) {
        super.removeModifier(p_188479_1_);
        this.inform();
    }

    @Override
    public void removeAllModifiers() {
        super.removeAllModifiers();
        this.inform();
    }

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
                .reduce(d1, MULTIPLICATION);
        d1 *= 1.0D + this.anonymous.get(2);

        return this.genericAttribute.clampValue(d1);
    }

    @Override
    protected Collection<AttributeModifier> getAppliedModifiers(int operation) {
        return super.getAppliedModifiers(operation);
    }

    public void correct() {
        this.setBaseValue(this.getAttribute().getDefaultValue());
    }

    public double getAnonymousModifier(int operation) {
        return this.anonymous.get(operation);
    }

    public void applyAnonymousModifier(int operation, double modifier) {
        this.anonymous.put(operation, modifier);
        this.needsUpdate = true;
        this.inform();
    }

    public IAttributeInstance addListener(IBaublesItemHandler handler) {
        this.handler = new WeakReference<>(handler);
        return this;
    }

    private void inform() {
        IBaublesItemHandler baubles = this.handler.get();
        if (baubles != null) {
            ((BaublesContainer) baubles).containerUpdated = false;
        }
    }
}
