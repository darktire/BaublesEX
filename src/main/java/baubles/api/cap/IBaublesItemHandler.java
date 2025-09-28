package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IBaublesItemHandler extends IItemHandlerModifiable {

    boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase entity);

	/**
	 * Used internally to prevent equip/unequip events from triggering when they shouldn't
	 */
    boolean isEventBlocked();
	void setEventBlock(boolean blockEvents);

	@Deprecated
	boolean isChanged(int slot);
	@Deprecated
	void setChanged(int slot, boolean changed);

	@Deprecated
    void setPlayer(EntityLivingBase entity);

	BaubleTypeEx getTypeInSlot(int index);

	int indexOf(Object o, int start);

	void setVisible(int slot, boolean v);
	boolean getVisible(int slot);
}
