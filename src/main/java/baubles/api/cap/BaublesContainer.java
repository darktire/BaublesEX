package baubles.api.cap;

import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

import static baubles.api.BaublesRegister.getSum;

public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private ArrayList<Boolean> changed;
    private boolean blockEvents = false;
    private EntityLivingBase player;

    public BaublesContainer() {
        super(getSum());
        this.changed = new ArrayList<>(stacks.size());
    }

    public void updateSlots(EntityPlayer player) {
        if (stacks.size() != getSum()) {
            NonNullList<ItemStack> stacks1 = stacks;
            stacks = NonNullList.withSize(getSum(), ItemStack.EMPTY);
            for (int i = 0; i < stacks1.size(); i++) {
                if (i < getSum()) {
                    super.stacks.set(i, stacks1.get(i));
                }
                else {
                    player.dropItem(stacks1.get(i), false);
                }
            }
        }
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
    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (stack.isEmpty() || this.isItemValidForSlot(slot, stack, player)) {
            super.stacks.set(slot, stack);
            setChanged(slot, true);
        }
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

    private void validateChanged(int slot) {
        if (changed == null) changed = new ArrayList<>(stacks.size());
        if (slot < 0) throw new RuntimeException("Bauble slot can't be negative" + '(' + slot + ')');
        if (slot >= changed.size()) changed.add(false);
    }

    @Override
    public boolean isChanged(int slot) {
        validateChanged(slot);
        return changed.get(slot);
    }

    @Override
    public void setChanged(int slot, boolean change) {
        validateChanged(slot);
        this.changed.set(slot, change);
    }

	@Override
	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < super.stacks.size(); i++) {
            if (!super.stacks.get(i).isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                super.stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", super.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
//        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < super.stacks.size()) {
                super.stacks.set(slot, new ItemStack(itemTags));
            }
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        setChanged(slot, true);
    }
}
