package baubles.api.registries;

import baubles.api.*;
import baubles.api.cap.BaubleItem;
import baubles.api.render.IRenderBauble;
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

    private static final Map<IBaubleKey, Function<ItemStack, IWrapper>> BAUBLE_ITEMS = new ConcurrentHashMap<>();
    private static final Cache<Equivalence.Wrapper<ItemStack>, IWrapper> CACHE = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    private static final LongAdder HIT  = new LongAdder();
    private static final LongAdder MISS = new LongAdder();
    private static final BaublesWrapper.CSTMap CST_MAP = BaublesWrapper.CSTMap.instance();

    public static void registerBauble(IBaubleKey key) {
        BAUBLE_ITEMS.put(key, BaublesWrapper::wrap);
    }

    public static void registerBauble(IBaubleKey key, IBauble bauble) {
        registerBauble(key);
        CST_MAP.update(key, attribute -> attribute.bauble(bauble));
    }

    public static void registerBauble(IBaubleKey key, List<BaubleTypeEx> types) {
        if (BAUBLE_ITEMS.containsKey(key)) {
            CST_MAP.update(key, attribute -> attribute.types(types));
        }
        else {
            registerBauble(key, new BaubleItem(types));
        }
    }

    /**
     * Register items with native supports.
     * @param item target item
     */
    public static void registerBauble(Item item) {
        registerBauble(IBaubleKey.BaubleKey.wrap(item));
    }

    /**
     * Link items and the bauble which is an instance of IBauble.
     * @param item target item
     * @param bauble bauble instanceof IBauble
     */
    public static void registerBauble(Item item, IBauble bauble) {
        registerBauble(IBaubleKey.BaubleKey.wrap(item), bauble);
    }

    /**
     * Simply register item as a bauble. (only with types)
     */
    public static void registerBauble(Item item, List<BaubleTypeEx> types) {
        registerBauble(IBaubleKey.BaubleKey.wrap(item), types);
    }

    /**
     * Simply register item as a bauble. (only with types)
     */
    public static void registerBauble(Item item, BaubleTypeEx... types) {
        registerBauble(item, Arrays.asList(types));
    }

    public static void registerRender(Item item, IRenderBauble render) {
        CST_MAP.update(item, attribute -> attribute.render(render));
    }

    public static void registerBauble(ItemStack stack) {
        registerBauble(IBaubleKey.BaubleKey.wrap(stack));
    }

    public static void registerBauble(ItemStack stack, IBauble bauble) {
        registerBauble(IBaubleKey.BaubleKey.wrap(stack), bauble);
    }

    public static void registerBauble(ItemStack stack, List<BaubleTypeEx> types) {
        registerBauble(IBaubleKey.BaubleKey.wrap(stack), types);
    }

    public static void registerBauble(ItemStack stack, BaubleTypeEx... types) {
        registerBauble(stack, Arrays.asList(types));
    }

    public static void registerRender(ItemStack stack, IRenderBauble render) {
        CST_MAP.update(stack, attribute -> attribute.render(render));
    }

    public static IWrapper toBauble(ItemStack stack) {
        Equivalence.Wrapper<ItemStack> cacheKey = IBaubleKey.CacheKey.getWrap(stack);

        IWrapper wrapper = CACHE.getIfPresent(cacheKey);
        if (wrapper == null) {
            IBaubleKey.BaubleKey key = IBaubleKey.BaubleKey.wrap(stack);
            Function<ItemStack, IWrapper> func = BAUBLE_ITEMS.get(key);
            if (func == null) {
                func = BAUBLE_ITEMS.get(key.fuzzier());
            }

            wrapper = func.apply(stack);
            CACHE.put(cacheKey, wrapper);
            MISS.increment();
        }
        else HIT.increment();
        return wrapper;
    }

    public static IBaubleKey trueBaubleKey(Item item) {
        return trueBaubleKey(IBaubleKey.BaubleKey.wrap(item), true);
    }

    public static IBaubleKey trueBaubleKey(IBaubleKey key, boolean isItem) {
        if (isItem) {
            return BAUBLE_ITEMS.keySet().stream()
                    .filter(get -> get.equals(key))
                    .findFirst()
                    .orElse(null);
        }
        else {
            IBaubleKey trueKey = BAUBLE_ITEMS.keySet().stream()
                    .filter(get -> get.equals(key))
                    .findFirst()
                    .orElse(null);
            if (trueKey == null) return trueBaubleKey(key.fuzzier(), true);
        }
        return null;
    }

    public static boolean isBauble(ItemStack stack) {
        return BAUBLE_ITEMS.containsKey(IBaubleKey.BaubleKey.wrap(stack.getItem())) || BAUBLE_ITEMS.containsKey(IBaubleKey.BaubleKey.wrap(stack));
    }

    public static boolean isBauble(Item item) {
        return BAUBLE_ITEMS.containsKey(IBaubleKey.BaubleKey.wrap(item).fuzzier());
    }

    public static String getStats() {
        return String.format("Baubles WrapperCache: hit=%d, miss=%d, content=%d", HIT.sum(), MISS.sum(), CACHE.size());
    }

    public static List<IBaubleKey> getList() {
        return new ArrayList<>(BAUBLE_ITEMS.keySet());
    }
}