package baubles.api;

import baubles.api.cap.IBaublesListener;
import baubles.api.render.IRenderBauble;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class AbstractWrapper implements IBauble, IRenderBauble, IBaublesListener {
    public final static class CSTMap {
        static final CSTMap INSTANCE = new CSTMap();
        private final Map<IBaubleKey, BaublesWrapper.Addition> map = new ConcurrentHashMap<>();

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
    }

    public final static class Addition {
        private final List<WeakReference<IBaublesListener>> listeners = new ArrayList<>();
        private final Object lock = new Object();
        IBauble bauble;
        IRenderBauble render;
        List<BaubleTypeEx> types;
        private boolean remove = false;

        private void broadcast() {
            List<IBaublesListener> snap;
            synchronized (lock) {
                listeners.removeIf(ref -> ref.get() == null);
                snap = new ArrayList<>(listeners.size());
                for (WeakReference<IBaublesListener> ref : listeners) {
                    IBaublesListener l = ref.get();
                    if (l != null) snap.add(l);
                }
            }
            for (IBaublesListener l : snap) {
                l.syncChanges();
            }
        }

        public void addListener(IBaublesListener listener) {
            synchronized (lock) {
                this.listeners.add(new WeakReference<>(listener));
            }
        }

        public void bauble(IBauble bauble) {
            this.bauble = bauble;
            this.broadcast();
        }

        public void render(IRenderBauble render) {
            this.render = render;
            this.broadcast();
        }

        public void types(List<BaubleTypeEx> types) {
            this.types = types;
            this.broadcast();
        }

        public void remove(boolean remove) {
            this.remove = remove;
            this.broadcast();
        }

        public static boolean isRemoved(ItemStack stack) {
            Addition addition = CSTMap.INSTANCE.get(stack);
            if (addition == null) return false;
            return addition.remove;
        }
    }
}
