package baubles.api;

import baubles.api.module.IModule;
import baubles.api.render.IRenderBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class AbstractWrapper implements IBauble, IRenderBauble {
    public final static class CSTMap {
        static final CSTMap INSTANCE = new CSTMap();
        private final Map<IBaubleKey, BaublesWrapper.Addition> map = new ConcurrentHashMap<>();
        private Map<IBaubleKey, BaublesWrapper.Addition> backup;

        public static CSTMap instance() {
            return INSTANCE;
        }

        public BaublesWrapper.Addition get(ItemStack stack) {
            IBaubleKey key = IBaubleKey.BaubleKey.wrap(stack);
            BaublesWrapper.Addition a = this.map.get(key);
            if (a == null) a = this.map.get(key.fuzzier());
            return a;
        }

        public <T> void update(ItemStack stack, BiConsumer<BaublesWrapper.Addition, T> editor, T param) {
            update(IBaubleKey.BaubleKey.wrap(stack), editor, param);
        }

        public <T> void update(Item item, BiConsumer<BaublesWrapper.Addition, T> editor, T param) {
            update(IBaubleKey.BaubleKey.wrap(item), editor, param);
        }

        public <T> void update(IBaubleKey key, BiConsumer<BaublesWrapper.Addition, T> editor, T param) {
            editor.accept(this.map.computeIfAbsent(key, i -> new BaublesWrapper.Addition()), param);
        }

        public void backup() {
            this.backup = new HashMap<>(this.map);
        }
        public void restore() {
            this.map.clear();
            this.map.putAll(this.backup);
        }
    }

    public final static class Addition {
        private boolean remove = false;
        IBauble bauble;
        IRenderBauble render;
        List<BaubleTypeEx> types;
        List<IModule> modules;

        public void bauble(IBauble bauble) {
            this.bauble = bauble;
        }

        public void render(IRenderBauble render) {
            this.render = render;
        }

        public void types(List<BaubleTypeEx> types) {
            this.types = types;
        }

        public void remove(boolean remove) {
            this.remove = remove;
        }

        public void modules(List<IModule> modules) {
            this.modules = modules;
        }

        public static boolean isRemoved(ItemStack stack) {
            Addition addition = CSTMap.INSTANCE.get(stack);
            if (addition == null) return false;
            return addition.remove;
        }
    }
}
