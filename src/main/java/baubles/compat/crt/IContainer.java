package baubles.compat.crt;

import baubles.Reference;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@IterableSimple("crafttweaker.item.IItemStack")
@ZenClass("mods." + Reference.MOD_ID + ".IContainer")
public interface IContainer extends crafttweaker.api.container.IContainer {

    @ZenMethod
    int indexOf(Object o, int start);

    @ZenMethod
    boolean isEquipped(Object o);

    @ZenMethod
    void modifySlot(String typeName, int modifier);

    @ZenMethod
    void configSlot(String typeName, int modifier);

    @ZenMethod
    String getTypeInSlot(int slot);
}
