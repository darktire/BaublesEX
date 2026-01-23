package baubles.compat.crt;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypeData;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketModifier;
import baubles.util.HookHelper;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class CrTContainer implements IContainer {
    private static final Map<IBaublesItemHandler, CrTContainer> CACHE = new WeakHashMap<>();
    private final IBaublesItemHandler baubles;
    private static final Field FIELD_STACKS = HookHelper.getField(ItemStackHandler.class, "stacks");

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
        BaubleTypeEx type = TypeData.getTypeByName(typeName);
        if (type != null) {
            EntityLivingBase owner = this.baubles.getOwner();
            AbstractAttributeMap map = owner.getAttributeMap();
            AdvancedInstance instance = AttributeManager.getInstance(map, type);
            double present = instance.getAnonymousModifier(0);
            instance.applyAnonymousModifier(0, present + modifier);
            PacketHandler.INSTANCE.sendTo(new PacketModifier(owner, typeName, (int) (present + modifier), 0), (EntityPlayerMP) owner);
        }
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
        return HookHelper.<NonNullList<ItemStack>>getValue(FIELD_STACKS, this.baubles).stream().map(CraftTweakerMC::getIItemStack).iterator();
    }
}
