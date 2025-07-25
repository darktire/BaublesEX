package baubles.api.cap;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;

import static baubles.api.BaublesRegister.getSum;

public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private HashMap<Integer, Boolean> changed;
    private boolean blockEvents = false;
    private EntityLivingBase player;

    public BaublesContainer() {
        super(getSum());
        this.changed = new HashMap<>(stacks.size());
    }

    /**
     * Update container when edit config in game
     */
    public void updateSlots(EntityPlayer player) {
        NonNullList<ItemStack> stacks1 = stacks;
        stacks = NonNullList.withSize(getSum(), ItemStack.EMPTY);
        for (int i = 0; i < stacks1.size(); i++) {
            if (i < getSum()) {
                super.stacks.set(i, stacks1.get(i));
            }
            else {
                player.dropItem(stacks1.get(i), false);
                //todo no effect
            }
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return super.getStackInSlot(slot);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
        if (stack == null || stack.isEmpty()) return false;
        IBauble bauble = BaublesApi.getBaubleItem(stack);
        if (bauble != null) {
            boolean canEquip = bauble.canEquip(stack, player);
            boolean hasSlot = bauble.getBaubleType().hasSlot(slot);
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
            super.setStackInSlot(slot, stack);
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

    @Override
    public boolean isChanged(int slot) {
        return changed.getOrDefault(slot, false);
    }

    @Override
    public void setChanged(int slot, boolean change) {
        this.changed.put(slot, change);
    }

	@Override
	public void setPlayer(EntityLivingBase player) {
		this.player = player;
	}

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < super.stacks.size(); i++) {
            ItemStack itemStack = super.stacks.get(i);
            if (!itemStack.isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                itemStack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", itemList);
        nbt.setInteger("Size", super.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
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
        this.setChanged(slot, true);
    }
}
