package baubles.api;

import com.google.common.base.Equivalence;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.Set;

public interface IBaubleKey {

    ItemStack ref();

    IBaubleKey fuzzier();

    class CacheKey extends Equivalence<ItemStack> {
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

        private static boolean isEqual(ItemStack a, ItemStack b) {
            if (a == b) return true;
            if (a == null || b == null) return false;
            return a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata() && Objects.equals(a.getTagCompound(), b.getTagCompound());
        }

        private static int getNbtHash(ItemStack stack) {
            NBTTagCompound nbt = stack.getTagCompound();
            return nbt == null ? 0 : nbt.hashCode();
        }
    }

    class BaubleKey implements IBaubleKey {
        private boolean fuzzy;
        private final Item item;
        private final ItemStack stack;

        private BaubleKey(Item item) {
            this.item = item;
            this.stack = null;
        }

        private BaubleKey(ItemStack stack) {
            this.item = stack.getItem();
            this.stack = stack;
        }

        public static BaubleKey wrap(Item item) {
            return new BaubleKey(item);
        }

        public static BaubleKey wrap(ItemStack stack) {
            return new BaubleKey(stack);
        }

        @Override
        public int hashCode() {
            return this.item.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof BaubleKey) {
                BaubleKey that = (BaubleKey) obj;

                return equivalent(this, that);
            }
            return false;
        }

        private static boolean equivalent(BaubleKey key1, BaubleKey key2) {
            if (key1.fuzzy || key2.fuzzy) {
                return key1.item == key2.item;
            }
            else {
                return isStackMatch(key1.stack, key2.stack);
            }
        }

        private static boolean isStackMatch(ItemStack a, ItemStack b) {
            if (a == b) return true;
            if (a == null || b == null) return false;
            return a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata() && isNbtMatch(b.getTagCompound(), a.getTagCompound());
        }

        private static boolean isNbtMatch(NBTTagCompound a, NBTTagCompound b) {
            if (a == null || a == b) return true;
            if (b == null) return false;
            Set<String> ref = a.getKeySet();
            if (b.getKeySet().containsAll(ref)) {
                return ref.stream().allMatch(key -> Objects.equals(a.getTag(key), b.getTag(key)));
            }
            return false;
        }

        @Override
        public ItemStack ref() {
            return stack == null ? new ItemStack(item) : stack;
        }

        @Override
        public IBaubleKey fuzzier() {
            this.fuzzy = true;
            return this;
        }
    }
}
