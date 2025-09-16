package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.IWrapper;
import baubles.api.cap.BaubleItem;
import baubles.api.render.IRenderBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Function;

public class ItemsData {

    private static final Map<Item, Function<ItemStack, IWrapper>> BAUBLE_ITEMS = new HashMap<>();
    private static final BaublesWrapper.CSTMap CST_MAP = BaublesWrapper.CSTMap.instance();

    /**
     * Register items with native supports.
     * @param item target item
     */
    public static void registerBauble(Item item) {
        BAUBLE_ITEMS.put(item, BaublesWrapper::instance);
    }

    /**
     * Link items and the bauble which is an instance of IBauble.
     * @param item target item
     * @param bauble bauble instanceof IBauble
     */
    public static void registerBauble(Item item, IBauble bauble) {
        registerBauble(item);
        CST_MAP.update(item, attribute -> attribute.bauble(bauble));
    }

    /**
     * Simply register item as a bauble. (only with types)
     */
    public static void registerBauble(Item item, BaubleTypeEx... types) {
        if (BAUBLE_ITEMS.containsKey(item)) {
            CST_MAP.update(item, attribute -> attribute.types(Arrays.asList(types)));
        }
        else {
            registerBauble(item, new BaubleItem(types));
        }
    }

    public static void registerRender(Item item, IRenderBauble render) {
        CST_MAP.update(item, attribute -> attribute.render(render));
    }

    public static IWrapper toBauble(ItemStack stack) {
        return BAUBLE_ITEMS.get(stack.getItem()).apply(stack);
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(item);
    }

    public static List<IWrapper> getList() {
        List<IWrapper> list = new ArrayList<>();
        BAUBLE_ITEMS.forEach(((item, function) -> list.add(function.apply(new ItemStack(item)))));
        return list;
    }

}
