package baubles.api;

import baubles.api.cap.BaubleItem;
import baubles.api.cap.IBaublesListener;
import baubles.api.event.BaublesEvent;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public final class BaublesWrapper implements IWrapper {
    private ItemStack stack;
    private IBauble bauble;
    private IRenderBauble render;
    private boolean edited = false;
    private Addition addition;

    public BaublesWrapper() {}

    public BaublesWrapper(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if (item instanceof IBauble) {
            this.bauble = (IBauble) item;
        }
        if (item instanceof IRenderBauble) {
            this.render = (IRenderBauble) item;
        }
        this.addition = CSTMap.INSTANCE.get(stack);
        if (this.addition != null) {
            this.addition.addListener(this);
        }
        this.syncChanges();
    }

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
        if (this.edited) return this.addition.types;
        return this.bauble.getTypes(itemStack);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        if (this.edited) return this.addition.types.get(0).getOldType();
        return this.bauble.getTypes(itemStack).get(0).getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.WearingTick event = new BaublesEvent.WearingTick(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onWornTick(itemstack, entity);
        if (this.bauble instanceof BaubleItem && entity instanceof EntityPlayer) {
            this.stack.getItem().onArmorTick(entity.world, (EntityPlayer) entity, this.stack);
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Equip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onEquipped(itemstack, entity);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent event = new BaublesEvent.Unequip.Post(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onUnequipped(itemstack, entity);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = this.bauble.canEquip(itemstack, entity);
        BaublesEvent.Equip.Pre event = new BaublesEvent.Equip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        boolean def = !EnchantmentHelper.hasBindingCurse(itemstack) && this.bauble.canUnequip(itemstack, entity);
        BaublesEvent.Unequip.Pre event = new BaublesEvent.Unequip.Pre(entity, itemstack, def);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
    }

    @Override
    public boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return this.bauble.canDrop(itemstack, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public <T extends IRenderBauble> List<T> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getSubRender(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getModel(stack, entity, renderPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this.render == null) return null;
        return this.render.getRenderType(stack, entity, renderPlayer);
    }

    @Override
    public void syncChanges() {
        if (this.addition != null) {
            if (this.addition.bauble != null) {
                this.bauble = this.addition.bauble;
            }
            if (this.addition.render != null) {
                this.render = this.addition.render;
            }
            if (this.addition.types != null) this.edited = true;
        }
    }

    public final static class CSTMap {
        private static final CSTMap INSTANCE = new CSTMap();
        private final Map<IBaubleKey, Addition> map = new ConcurrentHashMap<>();

        public static CSTMap instance() {
            return INSTANCE;
        }

        public Addition get(ItemStack stack) {
            IBaubleKey key = IBaubleKey.BaubleKey.wrap(stack);
            Addition a = this.map.get(key);
            if (a == null) a = this.map.get(key.fuzzier());
            return a;
        }

        public <T> void update(ItemStack stack, BiConsumer<Addition, T> editor, T param) {
            update(IBaubleKey.BaubleKey.wrap(stack), editor, param);
        }

        public <T> void update(Item item, BiConsumer<Addition, T> editor, T param) {
            update(IBaubleKey.BaubleKey.wrap(item), editor, param);
        }

        public <T> void update(IBaubleKey key, BiConsumer<Addition, T> editor, T param) {
            editor.accept(this.map.computeIfAbsent(key, i -> new Addition()), param);
        }
    }

    public final static class Addition {
        private final List<WeakReference<IBaublesListener>> listeners = new ArrayList<>();
        private final Object lock = new Object();
        private IBauble bauble;
        private IRenderBauble render;
        private List<BaubleTypeEx> types;
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
