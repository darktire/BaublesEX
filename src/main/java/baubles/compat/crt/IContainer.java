package baubles.compat.crt;

import baubles.api.BaublesApi;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@IterableSimple("crafttweaker.item.IItemStack")
@ZenClass("mods." + BaublesApi.MOD_ID + ".IContainer")
public interface IContainer extends crafttweaker.api.container.IContainer {

    @ZenMethod
    int indexOf(IItemStack crtStack, int start);

    @ZenMethod
    void modifySlot(String typeName, int modifier);

    @ZenMethod
    void configSlot(String typeName, int modifier);

    @ZenMethod
    String getTypeInSlot(int slot);
}
