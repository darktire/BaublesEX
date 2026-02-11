package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.BitSet;
import java.util.stream.IntStream;

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


	//---------------------------Modifiable---------------------------//


	void addListener(IBaublesListener listener);
	void removeListener(IBaublesListener listener);

	boolean canSync();

	/**
	 * Update stacks in container
	 */
	void updateContainer();

	EntityLivingBase getOwner();
	Monitor stx = new Monitor();
	Monitor vis = new Monitor();


	final class Monitor {
		final BitSet status = new BitSet();

		public void markDirty(int slot) {
			status.set(slot);
		}
		public boolean isDirty() {
			return !status.isEmpty();
		}

		public void clear() {
			status.clear();
		}

		public IntStream stream() {
			return status.stream();
		}

	}
}
