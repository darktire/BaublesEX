package baubles.api.registries;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.module.IModule;
import baubles.api.render.IRenderBauble;
import baubles.lib.util.ItemQuery;
import baubles.lib.util.TieredItemMatcher;
import com.google.common.base.Equivalence;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

public class ItemData {

    private static final TieredItemMatcher<Function<ItemStack, AbstractWrapper>> MATCHER = new TieredItemMatcher<>();
    private static final Cache<Equivalence.Wrapper<ItemStack>, AbstractWrapper> CACHE = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(600, TimeUnit.SECONDS)
            .build();
    private static final LongAdder HIT  = new LongAdder();
    private static final LongAdder MISS = new LongAdder();
    private static final BaublesWrapper.CSTMap CST_MAP = AbstractWrapper.CSTMap.instance();
    private static final Function<ItemStack, AbstractWrapper> DEFAULT = BaublesWrapper::new;

    private static final Equivalence<ItemStack> STACK_EQ = new Equivalence<>() {
        @Override
        protected boolean doEquivalent(ItemStack a, ItemStack b) {
            return a.getItem() == b.getItem() && meta(a) == meta(b) && Objects.equals(a.getTagCompound(), b.getTagCompound());
        }

        @Override
        protected int doHash(ItemStack stack) {
            return Objects.hash(stack.getItem(), meta(stack), stack.getTagCompound());
        }

        private static int meta(ItemStack stack) {
            return stack.getHasSubtypes() ? stack.getMetadata() : 0;
        }
    };

    public static void registerBauble(ItemQuery query) {
        MATCHER.register(query, DEFAULT);
    }

    public static void registerBauble(ItemQuery query, IBauble bauble) {
        registerBauble(query);
        CST_MAP.update(query, AbstractWrapper.Addition::bauble, bauble);
    }

    public static void registerBauble(ItemQuery query, List<BaubleTypeEx> types) {
        if (MATCHER.contains(query)) {
            CST_MAP.update(query, AbstractWrapper.Addition::types, types);
        }
        else {
            registerBauble(query, new BaubleItem(types));
        }
    }

    public static void registerBauble(ItemQuery query, BaubleTypeEx... types) {
        registerBauble(query, Arrays.asList(types));
    }

    public static void registerModules(ItemQuery query, List<IModule> modules) {
        CST_MAP.update(query, AbstractWrapper.Addition::modules, modules);
    }

    public static void registerRender(ItemQuery query, IRenderBauble render) {
        CST_MAP.update(query, AbstractWrapper.Addition::render, render);
    }

    /**
     * Register items with native supports.
     * @param item target item
     */
    public static void registerBauble(Item item) {
        registerBauble(ItemQuery.of(item));
    }

    public static AbstractWrapper toBauble(ItemStack stack) {
        Equivalence.Wrapper<ItemStack> cacheKey = STACK_EQ.wrap(stack);

        AbstractWrapper wrapper = CACHE.getIfPresent(cacheKey);
        if (wrapper != null) {
            HIT.increment();
            return wrapper;
        }

        Function<ItemStack, AbstractWrapper> func = MATCHER.match(stack);
        wrapper = (func != null ? func : DEFAULT).apply(stack);
        CACHE.put(cacheKey, wrapper);
        MISS.increment();
        return wrapper;
    }

    public static boolean isBauble(ItemQuery query) {
        return MATCHER.contains(query);
    }

    public static boolean isBauble(ItemStack stack) {
        return isBauble(ItemQuery.of(stack));
    }

    public static boolean isBauble(Item item) {
        return isBauble(ItemQuery.of(item));
    }

    public static List<ItemQuery> getList() {
        return MATCHER.getKeys();
    }

    public static String getStats() {
        return String.format("Baubles WrapperCache: hit=%d, miss=%d, content=%d", HIT.sum(), MISS.sum(), CACHE.size());
    }

    public static void backup() {
        MATCHER.backup();
        CST_MAP.backup();
    }

    public static void restore() {
        MATCHER.restore();
        CST_MAP.restore();
        CACHE.invalidateAll();
    }
}