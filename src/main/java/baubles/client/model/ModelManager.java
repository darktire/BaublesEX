package baubles.client.model;

import baubles.api.model.ModelBauble;
import com.google.common.base.Objects;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ModelManager {
    private static final Map<ModelKey, ModelBauble> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends ModelBauble> T getInstance(ItemStack stack, RenderPlayer renderPlayer, Function<ItemStack, T> f) {
        return (T) CACHE.putIfAbsent(new ModelKey(stack.getItem(), stack.getMetadata(), renderPlayer), f.apply(stack));
    }

    private static final class ModelKey {
        private final Item item;
        private final int meta;
        private final RenderPlayer render;
        ModelKey(Item item, int meta, RenderPlayer render) {
            this.item = item;
            this.meta = meta;
            this.render = render;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(item, meta, render);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof ModelKey) {
                ModelKey that = (ModelKey) obj;
                return this.item == that.item && this.meta == that.meta && this.render == that.render;
            }
            return false;
        }
    }
}
