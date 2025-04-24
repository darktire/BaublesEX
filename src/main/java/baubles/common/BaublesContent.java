package baubles.common;

import baubles.api.BaubleType;
import baubles.api.BaublesRegister;

import static baubles.common.Baubles.config;

public class BaublesContent extends BaublesRegister {
    public BaublesContent() {
        this.init();
        super.loadValidSlots();
    }
    @Override
    public void init() {
        super.init();
        for (BaubleType type : BaubleType.values()) {
            try {
                int value = config.getAmount(type.name());
                baubles.get(type.getTypeName()).setAmount(value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Baubles.log.error("BAUBLES default slots loading failed");
            }
        }
    }
}
