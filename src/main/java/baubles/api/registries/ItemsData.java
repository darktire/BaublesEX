package baubles.api.registries;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.IWrapper;
import baubles.api.cap.BaubleItem;
import baubles.api.render.IRenderBauble;
import baubles.api.util.MapKey;
import com.google.common.base.Equivalence;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

public class ItemsData {

    private static final Map<Item, Function<ItemStack, IWrapper>> BAUBLE_ITEMS = new ConcurrentHashMap<>();
    private static final Cache<Equivalence.Wrapper<ItemStack>, IWrapper> CACHE = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    private static final LongAdder HIT  = new LongAdder();
    private static final LongAdder MISS   = new LongAdder();
    private static final BaublesWrapper.CSTMap CST_MAP = BaublesWrapper.CSTMap.instance();

    /**
     * Register items with native supports.
     * @param item target item
     */
    public static void registerBauble(Item item) {
        BAUBLE_ITEMS.put(item, BaublesWrapper::wrap);
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
    public static void registerBauble(Item item, List<BaubleTypeEx> types) {
        if (BAUBLE_ITEMS.containsKey(item)) {
            CST_MAP.update(item, attribute -> attribute.types(types));
        }
        else {
            registerBauble(item, new BaubleItem(types));
        }
    }

    public static void registerBauble(Item item, BaubleTypeEx... types) {
        registerBauble(item, Arrays.asList(types));
    }

    public static void registerRender(Item item, IRenderBauble render) {
        CST_MAP.update(item, attribute -> attribute.render(render));
    }

    public static IWrapper toBauble(ItemStack stack) {
        Equivalence.Wrapper<ItemStack> cacheKey = MapKey.CacheKey.getWrap(stack);

        IWrapper wrapper = CACHE.getIfPresent(cacheKey);
        if (wrapper == null) {
            Function<ItemStack, IWrapper> func = BAUBLE_ITEMS.get(stack.getItem());

            wrapper = func.apply(stack);
            CACHE.put(cacheKey, wrapper);
            MISS.increment();
        }
        else HIT.increment();
        return wrapper;
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(item);
    }

    public static String getStats() {
        return String.format("Baubles WrapperCache: hit=%d, miss=%d, content=%d", HIT.sum(), MISS.sum(), CACHE.size());
    }

    public static List<IWrapper> getList() {
        List<IWrapper> list = new ArrayList<>();
        BAUBLE_ITEMS.forEach(((item, function) -> list.add(function.apply(new ItemStack(item)))));
        return list;
    }
}