package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.cap.BaubleItem;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemsData {

    private static final Map<Item, BaublesWrapper> BAUBLE_ITEMS = new HashMap<>();

    /**
     * Link items and the bauble which is a instance of IBauble
     * @param item item instanceof IBauble
     */
    public static void registerBauble(Item item) {
        BaublesWrapper wrapper = new BaublesWrapper(item);
        BAUBLE_ITEMS.put(item, wrapper);
    }

    /**
     * Link items and the bauble which is a instance of IBauble
     * @param wrapper wrapper of item and bauble
     */
    public static void registerBauble(BaublesWrapper wrapper) {
        BAUBLE_ITEMS.put(wrapper.getItem(), wrapper);
    }

    /**
     * Simply register item as a bauble
     */
    public static void registerBauble(Item item, BaubleTypeEx type) {
        registerBauble(new BaublesWrapper(item, new BaubleItem(type)));
    }

    public static BaublesWrapper toBauble(Item item) {
        return BAUBLE_ITEMS.get(item);
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(item);
    }
}
