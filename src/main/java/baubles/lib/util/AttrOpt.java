package baubles.lib.util;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public enum AttrOpt {
    ADDITION(0), MULTIPLY_BASE(1), MULTIPLY_TOTAL(2);

    private final int opt;
    private final int mask;

    AttrOpt(int opt) {
        this.opt = opt;
        this.mask = 1 << opt;
    }

    public int get() {
        return opt;
    }

    public int getMask() {
        return mask;
    }

    public static AttrOpt getOpt(AttributeModifier modifier) {
        return AttrOpt.values()[modifier.getOperation()];
    }
}
