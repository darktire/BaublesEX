package baubles.api.cap;

import baubles.api.IBauble;
import baubles.common.BaubleContent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private boolean[] changed;
    private boolean blockEvents = false;
    private EntityLivingBase player;

    public BaublesContainer() {
        super(BaubleContent.getAmount());
        this.changed = new boolean[stacks.size()];
    }

    protected void onContentsChanged(int slot) {
        setChanged(slot, true);
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
        if (stack == null || stack.isEmpty()) return false;
        IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        if (bauble != null) {
            boolean canEquip = bauble.canEquip(stack, player);
            boolean hasSlot = bauble.getBaubleType(stack).hasSlot(slot);
            return canEquip && hasSlot;
        }
        return false;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (stack.isEmpty() || this.isItemValidForSlot(slot, stack, player)) {
            validateSlotIndex(slot);
            this.stacks.set(slot, stack);
            setChanged(slot, true);
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.isItemValidForSlot(slot, stack, player)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isEventBlocked() {
        return blockEvents;
    }

    @Override
    public void setEventBlock(boolean blockEvents) {
        this.blockEvents = blockEvents;
    }

    @Override
    public boolean isChanged(int slot) {
        if (changed == null) {
            changed = new boolean[this.getSlots()];
        }
        return changed[slot];
    }

    @Override
    public void setChanged(int slot, boolean change) {
        if (changed == null) {
            changed = new boolean[this.getSlots()];
        }
        this.changed[slot] = change;
    }

	@Override
	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}
}
