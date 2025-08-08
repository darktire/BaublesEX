package baubles.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 *
 * This interface should be extended by items that can be worn in bauble slots
 *
 * @author Azanor
 */

public interface IBauble {

    /**
     * This method return the type of bauble this is.
     * Type is used to determine the slots it can go into.
     */
    default BaubleTypeEx getBaubleTypeEx() {
        return null;
    }

    /**
     * @deprecated prefer calling {@link IBauble#getBaubleTypeEx()} wherever possible
     */
    @Deprecated
    default BaubleType getBaubleType(ItemStack itemStack) {
        return null;
    }

    /**
     * This method is called once per tick if the bauble is being worn by a player
     */
    default void onWornTick(ItemStack itemstack, EntityLivingBase entity) {
    }

    /**
     * This method is called when the bauble is equipped by a entity
     */
    default void onEquipped(ItemStack itemstack, EntityLivingBase entity) {
    }

    /**
     * This method is called when the bauble is unequipped by a entity
     */
    default void onUnequipped(ItemStack itemstack, EntityLivingBase entity) {
    }

    /**
     * can this bauble be placed in a bauble slot
     */
    default boolean canEquip(ItemStack itemstack, EntityLivingBase entity) {
        return true;
    }

    /**
     * Can this bauble be removed from a bauble slot
     */
    default boolean canUnequip(ItemStack itemstack, EntityLivingBase entity) {
        return true;
    }

    /**
     * Will bauble automatically sync to client if a change is detected in its NBT or damage values?
     * Default is off, so override and set to true if you want to auto sync.
     * This sync is not instant, but occurs every 10 ticks (.5 seconds).
     */
    default boolean willAutoSync(ItemStack itemstack, EntityLivingBase entity) {
        return false;
    }

    default boolean canDrop(ItemStack itemstack, EntityLivingBase entity) {
        return true;
    }
}
