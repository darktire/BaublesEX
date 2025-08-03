package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.util.BaublesContent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class BaublesContainer extends ItemStackHandler implements IBaublesModifiable {

    private boolean blockEvents = false;
    private EntityLivingBase entity;
    private final HashMap<Integer, Boolean> changed = new HashMap<>();
    /**
     * Set the modified list of slots.
     */
    private final ArrayList<BaubleTypeEx> MODIFIED_SLOTS = new ArrayList<>();
    private final ArrayList<BaubleTypeEx> PREVIOUS_SLOTS = new ArrayList<>();
    private final HashMap<String, Integer> MODIFIER_FACTOR = new HashMap<>();
    private final HashMap<String, Integer> BAUBLE_MODIFIER = new HashMap<>();

    private boolean needUpdate = false;

    public BaublesContainer() { super(BaublesContent.getSum()); }

    public BaublesContainer(EntityLivingBase entity) {
        super(BaublesContent.getSum());
        this.entity = entity;
        this.MODIFIED_SLOTS.addAll(BaublesContent.getLazyList());
    }

    @Override
    public void modifySlot(String typeName, int modifier) {
        int original = BAUBLE_MODIFIER.getOrDefault(typeName, 0);
        modifySlotOA(typeName, modifier - original);
    }

    @Override
    public void modifySlotOA(String typeName, int modifier) {
        if (modifier == 0) return;
        PREVIOUS_SLOTS.clear();
        PREVIOUS_SLOTS.addAll(MODIFIED_SLOTS);
        BaubleTypeEx type = BaublesContent.getTypeByName(typeName);
        if (type == null) throw new RuntimeException("No such bauble type");
        int original = BAUBLE_MODIFIER.getOrDefault(typeName, 0);
        if (modifier > 0) {
            int index = MODIFIED_SLOTS.indexOf(type);
            if (index == -1 && original == 0) {
                for (int i = 1;; i++) {
                    BaubleTypeEx previous = BaublesContent.getTypeById(type.getId() - i);
                    index = MODIFIED_SLOTS.indexOf(previous) + 1;
                    if (index != 0) break;
                    if (type.getId() - i == 0) break;
                }
            }
            for (int i = 0; i < modifier; i++) {
                MODIFIED_SLOTS.add(index, type);
            }
        }
        else {
            int lastIndex = MODIFIED_SLOTS.lastIndexOf(type);
            if (lastIndex == -1) return;
            if (modifier + type.getAmount() + original < 0) modifier = -type.getAmount() - original;
            MODIFIED_SLOTS.subList(lastIndex + modifier + 1, lastIndex + 1).clear();
        }

        BAUBLE_MODIFIER.put(typeName, original + modifier);
        if (!needUpdate) needUpdate = true;
    }

    @Override
    public void clearModifier() {
        PREVIOUS_SLOTS.clear();
        PREVIOUS_SLOTS.addAll(MODIFIED_SLOTS);
        MODIFIED_SLOTS.clear();
        MODIFIED_SLOTS.addAll(BaublesContent.getLazyList());
        BAUBLE_MODIFIER.clear();
        needUpdate = true;
    }

    @Override
    public void updateSlots() {
        if (!BaublesContent.changed && !needUpdate) return;
        NonNullList<ItemStack> stacks1 = stacks;
        setSize(MODIFIED_SLOTS.size());
        boolean drop;
        for (int i = 0; i < stacks1.size(); i++) {
            ItemStack stack = stacks1.get(i);
            if (stack.isEmpty()) continue;
            BaubleTypeEx type = BaublesApi.toBauble(stack).getBaubleTypeEx();
            int start = MODIFIED_SLOTS.indexOf(type);
            drop = false;
            if (start == -1) drop = true;
            else {
                int newIndex = start - PREVIOUS_SLOTS.indexOf(type) + i;
                if (newIndex < MODIFIED_SLOTS.size() && MODIFIED_SLOTS.get(newIndex).equals(type)) {
                    stacks.set(newIndex, stack);
                }
                else drop = true;
            }
            if (drop && !entity.world.isRemote) entity.dropItem(stack.getItem(), stack.getCount());
        }
        if (BaublesContent.changed) BaublesContent.changed = false;
        if (needUpdate) needUpdate = false;
    }

    @Override
    public int getModifier(String typeName) {
        return BAUBLE_MODIFIER.getOrDefault(typeName, 0);
    }

    @Override
    public BaubleTypeEx getTypeInSlot(int slot) {
        return MODIFIED_SLOTS.get(slot);
    }

    @Override
    public ArrayList<Integer> getValidSlots(BaubleTypeEx type) {
        ArrayList<Integer> list = new ArrayList<>();
        int index = MODIFIED_SLOTS.indexOf(type);
        while (index != -1) {
            if (MODIFIED_SLOTS.subList(index, MODIFIED_SLOTS.size()).contains(type)) { list.add(index++); }
            else { break; }
        }
        return list;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot >= 0 && slot < this.stacks.size()) {
            return this.stacks.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase entity) {
        if (stack == null || stack.isEmpty()) return false;
        IBauble bauble = BaublesApi.toBauble(stack);
        if (bauble != null) {
            boolean canEquip = bauble.canEquip(stack, entity);
            boolean hasSlot = bauble.getBaubleTypeEx().equals(MODIFIED_SLOTS.get(slot));
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
        if (stack.isEmpty() || this.isItemValidForSlot(slot, stack, entity)) {
            super.setStackInSlot(slot, stack);
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.isItemValidForSlot(slot, stack, entity)) return stack;
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
	public void setPlayer(EntityLivingBase entity) {
		this.entity = entity;
	}

    @Override
    public NBTTagCompound serializeNBT() {
        // item persistence
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack itemStack = stacks.get(i);
            if (!itemStack.isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                itemStack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }
        // modifier persistence
        NBTTagCompound modifier = new NBTTagCompound();
        for (String typeName: BAUBLE_MODIFIER.keySet()) {
            int value = BAUBLE_MODIFIER.get(typeName);
            if (value != 0) modifier.setInteger(typeName, value);
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", itemList);
        nbt.setTag("Modifier", modifier);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Modifier")) {
            NBTTagCompound modifier = nbt.getCompoundTag("Modifier");
            for (String typeName: modifier.getKeySet()) {
                modifySlot(typeName, modifier.getInteger(typeName));
            }
            setSize(MODIFIED_SLOTS.size());
        }

        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTags = itemList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(slot, new ItemStack(itemTags));
            }
            //todo item out of size
        }
    }
}
