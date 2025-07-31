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

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

/**
 * @author Azanor
 */
public class BaublesApi {

    /**
     * Retrieves the baubles inventory capability handler for the supplied player
     */
    public static IBaublesModifiable getBaublesHandler(EntityLivingBase entity) {
        IBaublesModifiable handler = entity.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        if (handler != null) handler.updateSlots();
        return handler;
    }
    public static IBaublesItemHandler getBaublesHandler(EntityPlayer player) {
        return getBaublesHandler((EntityLivingBase) player);
    }

    /**
     * Retrieves the baubles capability handler wrapped as a IInventory for the supplied player
     */
    @Deprecated
    public static IInventory getBaubles(EntityPlayer player) {
        IBaublesModifiable handler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        if (handler != null) handler.updateSlots();
        return new BaublesInventoryWrapper(handler, player);
    }

    /**
     * Returns if the passed in item is equipped in a bauble slot. Will return the first slot found
     * @return -1 if not found and slot number if it is found
     */
    public static int isBaubleEquipped(EntityLivingBase entity, Item bauble) {
        IBaublesItemHandler handler = getBaublesHandler(entity);
        for (int a = 0; a < handler.getSlots(); a++) {
            if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem() == bauble) return a;
        }
        return -1;
    }
    public static int isBaubleEquipped(EntityPlayer player, Item bauble) {
        return isBaubleEquipped((EntityLivingBase) player, bauble);
    }

    public static Boolean isBauble(ItemStack stack) {
        return stack.hasCapability(CAPABILITY_ITEM_BAUBLE, null);
    }

    public static IBauble toBauble(ItemStack stack) {
        return stack.getCapability(CAPABILITY_ITEM_BAUBLE, null);
    }
}
