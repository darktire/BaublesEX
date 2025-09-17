package baubles.compat.crt;

import baubles.api.cap.IBaublesModifiable;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.WeakHashMap;

public class CrTContainer implements IContainer {
    private static final Map<IBaublesModifiable, CrTContainer> CACHE = new WeakHashMap<>();
    private final IBaublesModifiable baubles;

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
    public IItemStack getStackInSlot(int slot) {
        return CraftTweakerMC.getIItemStack(this.baubles.getStackInSlot(slot));
    }

    @Override
    public String getTypeInSlot(int slot) {
        return this.baubles.getTypeInSlot(slot).getTypeName();
    }
}
