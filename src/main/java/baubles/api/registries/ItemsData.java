package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.render.IRenderBauble;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ItemsData {

    private static final Map<Item, BaublesWrapper> BAUBLE_ITEMS = new HashMap<>();

    /**
     * Link items and the bauble which is an instance of IBauble.
     * @param item target item
     * @param wrapper wrapper of item and bauble
     */
    public static void registerBauble(Item item, BaublesWrapper wrapper) {
        if (BAUBLE_ITEMS.containsKey(item) && wrapper.getBauble() != null && !wrapper.isCopy()) {
            BAUBLE_ITEMS.get(item).setTypes(wrapper.getBaubleTypes());
        }
        else {
            BAUBLE_ITEMS.put(item, wrapper);
        }
    }

    /**
     * Link items and the bauble which is an instance of IBauble.
     * You can copy capability from an existed bauble to a item.
     * @param item target item
     * @param bauble bauble instanceof IBauble
     */
    public static void registerBauble(Item item, IBauble bauble) {
        registerBauble(item, new BaublesWrapper(item, bauble));
    }

    /**
     * Simply register item as a bauble. (only with types)
     */
    public static void registerBauble(Item item, BaubleTypeEx... types) {
        registerBauble(item, new BaublesWrapper(item, new BaubleItem(types)));
    }

    public static void registerBauble(Item item, IRenderBauble render) {
        BAUBLE_ITEMS.get(item).registerRender(render);
    }

    public static BaublesWrapper toBauble(Item item) {
        return BAUBLE_ITEMS.get(item);
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(item);
    }

    public static Iterator<BaublesWrapper> iterator() {
        return BAUBLE_ITEMS.values().iterator();
    }
}
