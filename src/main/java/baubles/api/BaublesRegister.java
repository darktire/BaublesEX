package baubles.api;

import java.util.HashMap;

public class BaublesRegister {

    protected static HashMap<String, BaubleTypeEx> baubles = new HashMap<>();
    protected static HashMap<Integer, BaubleTypeEx> baubleSlots = new HashMap<>();
    protected static int sum;

    public BaublesRegister() {
        init();
        loadValidSlots();
    }

    public void init() {
        for (BaubleType type : BaubleType.values()) {
            baubles.put(type.getTypeName(), type.getBaubleTypeEx());
        }
    }

    public void registerBauble(String typeName, int amount) {
        baubles.put(typeName, new BaubleTypeEx(typeName, amount));
    }

    public void loadValidSlots() {
        int pointer = 0;
        for (BaubleTypeEx type : baubles.values()) {
            int amount = type.getAmount();
            int[] list = new int[amount];
            for (int i = 0; i < amount; i++) {
                int index = pointer + i;
                list[i] = index;
                baubleSlots.put(index, type);
            }
            type.setValidSlots(list);
            pointer += amount;
        }
        sum = pointer;
    }

    public static HashMap<Integer, BaubleTypeEx> getSlots() {
        return baubleSlots;
    }

    public static int getSum() {
        return sum;
    }
}
