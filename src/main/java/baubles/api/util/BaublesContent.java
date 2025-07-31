package baubles.api.util;

import baubles.api.BaubleTypeEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class BaublesContent{
    /**
     * Simply summarise.
     */
    private static int sum = 7;
    public static boolean changed = false;
    /**
     * Dynamic bauble types.
     */
    private static final HashMap<String, BaubleTypeEx> BAUBLE_TYPES = new HashMap<>();
    /**
     * Set the order of baubles.
     */
    private static final LinkedList<BaubleTypeEx> BAUBLE_ORDER = new LinkedList<>();
    /**
     * Set the lazy list of slots.
     */
    private static final ArrayList<BaubleTypeEx> BAUBLE_SLOTS = new ArrayList<>();

    public static void registerBauble(BaubleTypeEx type, int amount) {
        if (amount < 0) amount = 0;
        if (BAUBLE_TYPES.containsKey(type.getTypeName())) {
            type.setAmount(amount);
        }
        else {
            registerBauble(type);
        }
    }

    public static void registerBauble(String typeName, int amount) {
        registerBauble(new BaubleTypeEx(typeName, amount), amount);
    }

    public static void registerBauble(BaubleTypeEx type) {
        BAUBLE_TYPES.put(type.getTypeName(), type);
        BAUBLE_ORDER.add(type);
    }

    public static void setSum(int value) {
        sum = value;
    }
    public static int getSum() {
        return sum;
    }

    public static void initLazyList() {
        if(!BAUBLE_SLOTS.isEmpty()) BAUBLE_SLOTS.clear();
    }
    public static void addLazySlots(BaubleTypeEx type) {
        BAUBLE_SLOTS.add(type);
    }
    public static ArrayList<BaubleTypeEx> getLazyList() {
        return BAUBLE_SLOTS;
    }

    public static BaubleTypeEx getTypeByName(String typeName) {
        return BAUBLE_TYPES.get(typeName);
    }
    public static BaubleTypeEx getTypeById(int id) {
        return BAUBLE_ORDER.get(id);
    }

    public static boolean hasType(String typeName) {
        return BAUBLE_TYPES.containsKey(typeName);
    }

    public static Iterator<BaubleTypeEx> iterator() {
        return BAUBLE_ORDER.iterator();
    }
}
//todo capability to modify slots