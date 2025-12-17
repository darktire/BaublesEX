package baubles.compat.crt;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class CrTContainer implements IContainer {
    private static final Map<IBaublesItemHandler, CrTContainer> CACHE = new WeakHashMap<>();
    private final IBaublesItemHandler baubles;
    private static final Field FIELD_STACKS = HookHelper.getField("net.minecraftforge.items.ItemStackHandler", "stacks");

    private CrTContainer(IBaublesItemHandler baubles) {
        this.baubles = baubles;
    }

    public static CrTContainer of(IBaublesItemHandler baubles) {
        return CACHE.computeIfAbsent(baubles, CrTContainer::new);
    }

    @Override
    public int indexOf(Object o, int start) {
        if (o instanceof IItemStack) {
            Object stack = CraftTweakerMC.getItemStack((IItemStack) o);
            return this.baubles.indexOf(stack, start);
        }
        else if (o instanceof IItemDefinition) {
            Object item = ((IItemDefinition) o).getInternal();
            return this.baubles.indexOf(item, start);
        }
        return -1;
    }

    @Override
    public void modifySlot(String typeName, int modifier) {
        this.baubles.modifySlot(typeName, modifier);
    }

    @Override
    public void configSlot(String typeName, int modifier) {
        HookHelper.configSlot(typeName, modifier, true);
    }

    @Override
    public int getContainerSize() {
        return this.baubles.getSlots();
    }

    @Override
    public IItemStack getStack(int slot) {
        return CraftTweakerMC.getIItemStack(this.baubles.getStackInSlot(slot));
    }

    @Override
    public void setStack(int i, IItemStack crtStack) {
        this.baubles.setStackInSlot(i, CraftTweakerMC.getItemStack(crtStack));
    }

    @Override
    public String asString() {
        return BaublesApi.MOD_ID;
    }

    @Override
    public Object getInternal() {
        return this.baubles;
    }

    @Override
    public String getTypeInSlot(int slot) {
        return this.baubles.getTypeInSlot(slot).getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<IItemStack> iterator() {
        return ((NonNullList<ItemStack>) HookHelper.getValue(FIELD_STACKS, this.baubles)).stream().map(CraftTweakerMC::getIItemStack).iterator();
    }
}
