package baubles.compat.crt;

import baubles.api.cap.IBaublesModifiable;
import baubles.util.HookHelper;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class CrTContainer implements IContainer {
    private static final Map<IBaublesModifiable, CrTContainer> CACHE = new WeakHashMap<>();
    private final IBaublesModifiable baubles;
    private static final Field FIELD_STACKS = HookHelper.getField("net.minecraftforge.items.ItemStackHandler", "stacks");

    private CrTContainer(IBaublesModifiable baubles) {
        this.baubles = baubles;
    }

    public static CrTContainer of(IBaublesModifiable baubles) {
        return CACHE.computeIfAbsent(baubles, CrTContainer::new);
    }

    @Override
    public int indexOf(IItemStack crtStack, int start) {
        ItemStack stack = CraftTweakerMC.getItemStack(crtStack);
        return this.baubles.indexOf(stack, start);
    }

    @Override
    public void modifySlotOA(String typeName, int modifier) {
        this.baubles.modifySlotOA(typeName, modifier);
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
        return null;
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
