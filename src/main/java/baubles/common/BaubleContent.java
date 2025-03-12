package baubles.common;

import baubles.api.BaubleType;
import baubles.api.BaubleTypeEx;

public class BaubleContent extends BaubleTypeEx {
    private static final Class<Config> clazz = Config.class;
    public BaubleContent(String type, int amount) {
        super(type, amount);
    }

    public static void initSlots() {
        slots.clear();
        for (BaubleType type : BaubleType.values()) {
            if (type != BaubleType.TRINKET) {
                Object value;
                try {
                    value = clazz.getDeclaredField(type.name()).get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Baubles.log.error("BAUBLES default slots loading failed");
                    throw new RuntimeException(e);
                }
                new BaubleTypeEx(type.name, (int)value);
            }
        }
    }

    public static int getAmount() {
        return slots.size();
    }
}
