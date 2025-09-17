package baubles.compat.crt;

import baubles.Baubles;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Baubles.MOD_ID + ".IContainer")
public interface IContainer {

    @ZenMethod
    int indexOf(IItemStack crtStack, int start);

    @ZenMethod
    void modifySlotOA(String typeName, int modifier);

    @ZenMethod
    IItemStack getStackInSlot(int slot);

    @ZenMethod
    String getTypeInSlot(int slot);
}
