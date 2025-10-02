package baubles.api.util;

import com.google.common.base.Equivalence;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class MapKey {

    private static boolean isEqual(ItemStack a, ItemStack b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata() && getNbtHash(a) == getNbtHash(b);
    }

    private static int getNbtHash(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return 0;
        return nbt.hashCode();
    }

    public static class CacheKey extends Equivalence<ItemStack> {
        private static final CacheKey INSTANCE = new CacheKey();

        public static Wrapper<ItemStack> getWrap(ItemStack stack) {
            return INSTANCE.wrap(stack);
        }

        @Override
        protected boolean doEquivalent(ItemStack a, ItemStack b) {
            return isEqual(a, b);
        }

        @Override
        protected int doHash(ItemStack stack) {
            return Objects.hash(stack.getItem(), stack.getMetadata(), getNbtHash(stack));
        }
    }

    public static class CrossKey {
        private final boolean fuzzy;
        private final Item item;
        private final ItemStack stack;

        private CrossKey(Item item) {
            this.fuzzy = true;
            this.item = item;
            this.stack = null;
        }

        private CrossKey(ItemStack stack) {
            this.fuzzy = false;
            this.item = stack.getItem();
            this.stack = stack;
        }

        public static CrossKey wrap(Item item) {
            return new CrossKey(item);
        }

        public static CrossKey wrap(ItemStack stack) {
            return new CrossKey(stack);
        }

        @Override
        public int hashCode() {
            return this.item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof CrossKey) {
                CrossKey that = (CrossKey) obj;
                return equivalent(this, that);
            }
            return false;
        }

        private static boolean equivalent(CrossKey key1, CrossKey key2) {
            if (key1.fuzzy || key2.fuzzy) {
                return key1.item == key2.item;
            }
            else {
                return isEqual(key1.stack, key2.stack);
            }
        }
    }
}
