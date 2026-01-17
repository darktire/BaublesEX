package baubles.api.attribute;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class NonNegativeAttribute extends RangedAttribute {

    public NonNegativeAttribute(IAttribute parentIn, String name, double defaultValue) {
        super(parentIn, name, defaultValue, 0, Float.MAX_VALUE);
    }

    public void setDefaultValue(double value) {
        this.defaultValue = value;
    }
}
