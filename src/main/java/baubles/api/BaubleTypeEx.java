package baubles.api;

import java.util.ArrayList;

public class BaubleTypeEx {

    protected static ArrayList<String> slots = new ArrayList<>();
    public String type;
    public int amount;

    public BaubleTypeEx(String type, int amount) {
        this.type = type;
        this.amount = amount;
        registerType(type, amount);
    }

    /**
     * Without register type.
     */
    public BaubleTypeEx(String type) {
        this.type = type;
    }

    protected void registerType(String type, int amount) {
        for (int i = 0; i < amount; i++){
            slots.add(type);
        }
    }

    public boolean hasSlot(int slot) {
        for (int s : this.getValidSlots()) {
            if (s == slot) return true;
        }
        return false;
    }

    public int[] getValidSlots() {
        int[] validSlots = new int[0];
        if (slots.contains(type)) {
            int min = slots.indexOf(type);
            int amount = slots.lastIndexOf(type) - min + 1;
            validSlots = new int[amount];
            for (int i = 0; i < amount; i++) validSlots[i] = min + i;
        }
        if (type.equals("trinket")) {
            int length = slots.size();
            validSlots = new int[length];
            for (int i = 0; i < length; i++) validSlots[i] = i;
        }

        return validSlots;
    }

    public static ArrayList<String> getSlots() {
        return slots;
    }
}

//todo type define