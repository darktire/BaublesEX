package baubles.api;

import java.util.HashMap;
import java.util.Iterator;

public class BaublesRegister {
    /**
     * Dynamic bauble types.
     */
    protected static HashMap<String, BaubleTypeEx> baubles = new HashMap<>();
    /**
     * Help to get type by index.
     */
    protected static HashMap<Integer, BaubleTypeEx> baubleSlots = new HashMap<>();
    /**
     * Simply summarise.
     */
    protected static int sum = 7;

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

    public void registerBauble(String typeName, BaubleTypeEx type) {
        baubles.put(typeName, type);
    }

    /**
     * Set baubleSlots, validSlots.
     */
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
    }

    public BaubleTypeEx getBaubles(String typeName) {
        return baubles.get(typeName);
    }

    public BaubleTypeEx getSlot(int index) {
        return baubleSlots.get(index);
    }

    public static int getSum() {
        return sum;
    }

    public Iterator<BaubleTypeEx> iterator() {
        return baubles.values().iterator();
    }
}
