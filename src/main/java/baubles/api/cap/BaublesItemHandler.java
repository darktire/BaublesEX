package baubles.api.cap;

import baubles.api.IBauble;
import baubles.common.BaubleContent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class BaublesItemHandler extends ItemStackHandler implements IBaublesItemHandler {

    private final int amount = BaubleContent.getAmount();
    private int offset = 0;
    private boolean[] changed;
    private boolean blockEvents = false;
    private EntityLivingBase player;

    public BaublesItemHandler() {
        super(BaubleContent.getAmount());
        this.changed = new boolean[stacks.size()];
    }

    public void resetOffset() {
        offset = 0;
    }
    public int setSlot(int slot) {
        int slotSet = offset + slot;
        if (slotSet >= amount) slotSet %= amount;
        return slotSet;
    }

    public void incrOffset(int incr) {
        offset += incr;
        offset %= amount;
        if (offset < 0) offset += amount;
    }

    protected void onContentsChanged(int slot) {
        slot = setSlot(slot);
        setChanged(slot, true);
    }

    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
        slot = setSlot(slot);
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
            slot = setSlot(slot);
            validateSlotIndex(slot);
            this.stacks.set(slot, stack);
            setChanged(slot, true);
        }
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        slot = setSlot(slot);
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.isItemValidForSlot(slot, stack, player)) return stack;
        slot = setSlot(slot);
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        slot = setSlot(slot);
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        slot = setSlot(slot);
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
        slot = setSlot(slot);
        if (changed == null) {
            changed = new boolean[this.getSlots()];
        }
        return changed[slot];
    }

    @Override
    public void setChanged(int slot, boolean change) {
        slot = setSlot(slot);
        if (changed == null) {
            changed = new boolean[this.getSlots()];
        }
        this.changed[slot] = change;
    }

    @Override
    public void setPlayer(EntityLivingBase player) {
        this.player = player;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        slot = setSlot(slot);
        return super.isItemValid(slot, stack);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
//        if (!nbt.hasKey("Size", Constants.NBT.TAG_INT)) setSize(stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            ItemStack stack = new ItemStack(itemTags);

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, stack);
            }
            else if (slot >= stacks.size()) {
                ((EntityPlayer)player).addItemStackToInventory(stack);
                IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                if (bauble != null) {
                    bauble.onUnequipped(stack, player);
                }
            }
        }
        onLoad();
    }
}