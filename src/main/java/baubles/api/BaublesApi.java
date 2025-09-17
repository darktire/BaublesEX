package baubles.api;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.inv.BaublesInventoryWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BaublesApi {

    /**
     * Retrieves the baubles inventory capability handler for the supplied player
     */
    public static IBaublesModifiable getBaublesHandler(EntityLivingBase entity) {
        IBaublesModifiable handler = entity.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        if (handler != null) handler.updateContainer();
        return handler;
    }

    /**
     * @deprecated prefer calling {@link BaublesApi#getBaublesHandler(EntityLivingBase)} wherever possible
     */
    @Deprecated
    public static IBaublesItemHandler getBaublesHandler(EntityPlayer player) {
        return getBaublesHandler((EntityLivingBase) player);
    }

    /**
     * Retrieves the baubles capability handler wrapped as a IInventory for the supplied player
     */
    @Deprecated
    public static IInventory getBaubles(EntityPlayer player) {
        IBaublesModifiable handler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        if (handler != null) handler.updateContainer();
        return new BaublesInventoryWrapper(handler, player);
    }

    /**
     * Get the index of the stack, the item or the type in baubles.
     * @return -1 if not found and slot number if it is found
     */
    public static int getIndexInBaubles(EntityLivingBase entity, Object o, int start) {
        IBaublesModifiable baubles = getBaublesHandler(entity);
        if (baubles != null) {
            return baubles.indexOf(o, start);
        }
        return -1;
    }

    /**
     * @deprecated prefer calling {@link BaublesApi#getIndexInBaubles(EntityLivingBase, Object, int)} wherever possible
     */
    @Deprecated
    public static int isBaubleEquipped(EntityPlayer player, Item bauble) {
        return getIndexInBaubles(player, bauble, 0);
    }

    public static boolean canEquipBaubles(EntityLivingBase entity) {
        return entity.hasCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
    }

    public static boolean isBauble(ItemStack stack) {
        return stack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
    }

    /**
     * Use this method to get cap from stack
     * @return null when stack doesn't have baubles:item_cap
     */
    public static IWrapper toBauble(ItemStack stack) {
        return stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
    }

    public static void applyByIndex(EntityLivingBase entity, BiConsumer<IBaublesModifiable, Integer> c) {
        IBaublesModifiable baubles = getBaublesHandler(entity);
        for (int i = 0; i < baubles.getSlots(); i++) {
            c.accept(baubles, i);
        }
    }

    public static void applyToBaubles(EntityLivingBase entity, Consumer<ItemStack> c) {
        applyByIndex(entity, (baubles, i) -> c.accept(baubles.getStackInSlot(i)));
    }
}
