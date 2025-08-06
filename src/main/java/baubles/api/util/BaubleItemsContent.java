package baubles.api.util;

import baubles.api.BaubleTypeEx;
import baubles.api.cap.BaubleItem;
import net.minecraft.item.Item;

import java.util.HashMap;

public class BaubleItemsContent {
    /**
     * Dynamic bauble items.
     */
    private static final HashMap<Item, BaublesWrapper> BAUBLE_ITEMS = new HashMap<>();

    public static void registerBauble(Item item) {
        BAUBLE_ITEMS.put(item, new BaublesWrapper(item));
    }

    public static void registerBauble(BaubleItem baubleItem) {
        BAUBLE_ITEMS.put(baubleItem.getItem(), new BaublesWrapper(baubleItem));
    }

    public static void registerBauble(Item item, BaubleTypeEx type) {
        registerBauble(new BaubleItem(item, type));
    }

    public static BaublesWrapper toBauble(Item item) {
        return BAUBLE_ITEMS.get(item);
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(item);
    }
}
