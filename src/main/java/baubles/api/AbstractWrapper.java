package baubles.api;

import baubles.api.module.IModule;
import baubles.api.render.IRenderBauble;
import baubles.lib.util.ItemQuery;
import baubles.lib.util.TieredItemMatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractWrapper implements IBauble, IRenderBauble {
    public final static class CSTMap {
        static final CSTMap INSTANCE = new CSTMap();
        private final TieredItemMatcher<Addition> matcher = new TieredItemMatcher<>();

        public static CSTMap instance() { return INSTANCE; }

        public BaublesWrapper.Addition get(ItemStack stack) {
            return matcher.match(stack);
        }

        public <T> void update(ItemQuery query, BiConsumer<BaublesWrapper.Addition, T> editor, T param) {
            editor.accept(matcher.computeIfAbsent(query, BaublesWrapper.Addition::new), param);
        }

        public void backup() { matcher.backup(); }
        public void restore() { matcher.restore(); }
    }

    public final static class Addition {
        private boolean remove = false;
        IBauble bauble;
        IRenderBauble render;
        List<BaubleTypeEx> types;
        List<IModule> modules;
        List<WornTickEffect> effects;

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

        public void effects(List<WornTickEffect> effects) {
            this.effects = effects;
        }

    }

    @FunctionalInterface
    public interface WornTickEffect {
        void tick(ItemStack worn, EntityLivingBase entity);

        static WornTickEffect armor(ItemQuery query) {
            return (worn, entity) -> {
                if (!(entity instanceof EntityPlayer)) return;
                ItemStack target = query != null ? query.ref() : worn;
                target.getItem().onArmorTick(entity.world, (EntityPlayer) entity, target);
            };
        }

        static WornTickEffect passive(ItemQuery query) {
            return (worn, entity) -> {
                ItemStack target = query != null ? query.ref() : worn;
                target.getItem().onUpdate(target, entity.world, entity, 0, false);
            };
        }

        static WornTickEffect inUse(ItemQuery query) {
            return (worn, entity) -> {
                ItemStack target = query != null ? query.ref() : worn;
                target.getItem().onUsingTick(target, entity, 0);
            };
        }
    }
}
