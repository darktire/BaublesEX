package baubles.api;

import baubles.api.cap.IBaublesListener;
import baubles.api.event.BaublesEvent;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.api.util.MapKey;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class BaublesWrapper implements IWrapper {
    private ItemStack stack;
    private IBauble bauble;
    private IRenderBauble render;
    private boolean edited = false;
    private Attribute attribute;

    public BaublesWrapper() {}

    private BaublesWrapper(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if (item instanceof IBauble) {
            this.bauble = (IBauble) item;
        }
        if (item instanceof IRenderBauble) {
            this.render = (IRenderBauble) item;
        }
        this.attribute = CSTMap.INSTANCE.get(stack);
        this.updateBaubles();
    }

    public static IWrapper wrap(ItemStack stack) {
        return new BaublesWrapper(stack).startListening();
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    @Override
    public List<BaubleTypeEx> getTypes(ItemStack itemStack) {
        if (this.edited) return this.attribute.types;
        return this.bauble.getTypes(itemStack);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        if (this.edited) return this.attribute.types.get(0).getOldType();
        return this.bauble.getTypes(itemStack).get(0).getOldType();
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
        BaublesEvent.WearingTick event = new BaublesEvent.WearingTick(entity, itemstack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        this.bauble.onWornTick(itemstack, entity);
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
    public Map<ModelBauble, RenderType> getRenderMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getRenderMap(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getModel(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getTexture(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getEmissiveMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getEmissiveMap(stack, entity, slim);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderType getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this.render == null) return null;
        return this.render.getRenderType(stack, entity, slim);
    }

    @Override
    public void updateBaubles() {
        if (this.attribute != null) {
            if (this.attribute.bauble != null) {
                this.bauble = this.attribute.bauble;
            }
            if (this.attribute.render != null) {
                this.render = this.attribute.render;
            }
            if (this.attribute.types != null) this.edited = true;
        }
    }

    @Override
    public IWrapper startListening() {
        Attribute attribute = CSTMap.INSTANCE.get(this.stack);
        if (attribute != null) {
            attribute.addListener(this);
        }
        return this;
    }

    public final static class CSTMap {
        private static final CSTMap INSTANCE = new CSTMap();
        private final Map<MapKey.CrossKey, BaublesWrapper.Attribute> map = new ConcurrentHashMap<>();

        public static CSTMap instance() {
            return INSTANCE;
        }

        public BaublesWrapper.Attribute get(ItemStack stack) {
            return this.map.get(MapKey.CrossKey.wrap(stack));
        }

        public void update(ItemStack stack, Consumer<BaublesWrapper.Attribute> editor) {
            editor.accept(this.map.computeIfAbsent(MapKey.CrossKey.wrap(stack), i -> new BaublesWrapper.Attribute()));
        }

        public void update(Item item, Consumer<BaublesWrapper.Attribute> editor) {
            editor.accept(this.map.computeIfAbsent(MapKey.CrossKey.wrap(item), i -> new BaublesWrapper.Attribute()));
        }
    }

    public final static class Attribute {
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
                l.updateBaubles();
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
            BaublesWrapper.Attribute attribute = CSTMap.INSTANCE.get(stack);
            if (attribute == null) return false;
            return attribute.remove;
        }
    }
}
