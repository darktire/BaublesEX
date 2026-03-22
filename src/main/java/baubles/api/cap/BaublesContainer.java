package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.attribute.AttributeManager;
import baubles.api.module.ModuleCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.function.Predicate;

public class BaublesContainer extends ItemStackHandler implements IBaublesItemHandler {

    private boolean blockEvents = false;
    private EntityLivingBase entity;

    private final List<BaubleTypeEx> MODIFIED_SLOTS = new ArrayList<>();
    private final List<BaubleTypeEx> PREVIOUS_SLOTS = new ArrayList<>();

    private List<ItemStack> snapshot;
    private BitSet visibility = new BitSet();
    private ModuleCore core = new ModuleCore();

    private final Queue<ItemStack> dropQue = new ArrayDeque<>();
    private final Set<IBaublesListener> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    public boolean containerUpdated = true;
    private Runnable respawnTask = this::dropItems;
    private boolean syncLock = false;

    public BaublesContainer() { super(0); }

    public BaublesContainer(EntityLivingBase entity) {
        super(0);
        this.entity = entity;
    }

    @Override
    public void updateContainer() {
        while (!this.containerUpdated) {
            this.syncSlots();
            this.onSlotChanged();
            this.dropItems();
        }
        if (dropQue.isEmpty()) {
            this.syncLock = false;
            if (!this.entity.world.isRemote) {
                this.stx.clear();
                this.stx.markDirty(0, this.getSlots());
            }
            this.listeners.forEach(IBaublesListener::syncChanges);
        } else {
            this.syncLock = true;
        }
    }

    private void syncSlots() {
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(AttributeManager.computeSlots(entity));

    }

    private void onSlotChanged() {
        BitSet vis = (BitSet) this.visibility.clone();

        int size = this.MODIFIED_SLOTS.size();
        this.snapshot = this.stacks;
        setSize(size);

        List<ItemStack> temp = new ArrayList<>(this.stacks);
        this.visibility.clear();

        for (int i = 0; i < this.snapshot.size(); i++) {
            boolean inv = !vis.get(i);
            ItemStack stack = this.snapshot.get(i);

            if (stack.isEmpty() && inv) continue;

            BaubleTypeEx type = this.PREVIOUS_SLOTS.get(i);
            int oldPos = this.PREVIOUS_SLOTS.indexOf(type);
            int newPos = this.MODIFIED_SLOTS.indexOf(type);

            if (newPos == -1) {
                dropQue.add(stack);
                continue;
            }

            int delta = newPos - oldPos;
            int mappedIndex = i + delta;

            if (mappedIndex < size && this.MODIFIED_SLOTS.get(mappedIndex) == type) {
                if (!stack.isEmpty()) {
                    this.stacks.set(mappedIndex, stack);
                    temp.set(mappedIndex, stack.copy());
                }
                if (!inv) this.visibility.set(mappedIndex);
            } else {
                dropQue.add(stack);
            }
        }

        this.snapshot = temp;
        this.containerUpdated = true;
    }

    @Override
    public BaubleTypeEx getTypeInSlot(int slot) {
        return MODIFIED_SLOTS.get(slot);
    }

    @Override
    public int indexOf(Object o, int start) {
        if (o instanceof ItemStack) return indexOf(this.stacks, start, stack -> ItemStack.areItemStacksEqual(stack, (ItemStack) o));
        else if (o instanceof Item) return indexOf(this.stacks, start, stack -> stack.getItem() == o);
        else if (o instanceof BaubleTypeEx) return indexOf(this.MODIFIED_SLOTS, start, type -> type.contains((BaubleTypeEx) o));
        return -1;
    }

    private static <T> int indexOf(List<T> list, int start, Predicate<? super T> predicate) {
        for (int i = start, max = list.size(); i < max; i++) {
            if (predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot >= 0 && slot < this.stacks.size()) {
            return this.stacks.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void onContentsChanged(int slot) {
        if (!this.entity.world.isRemote) {
            ItemStack stack = this.snapshot.get(slot);
            ItemStack stack1 = this.stacks.get(slot);
            if (!ItemStack.areItemStacksEqual(stack, stack1)) {
                this.stx.markDirty(slot);
                this.snapshot.set(slot, stack1.copy());
                onExchange(this.entity, stack, stack1);
            }
        }
    }

    private void onExchange(EntityLivingBase entity, ItemStack stack, ItemStack stack1) {
        if (!stack.isEmpty()) {
            this.core.batchDecrement(BaublesApi.toBauble(stack).getModules(stack, entity));
        }
        if (!stack1.isEmpty()) {
            this.core.batchIncrement(BaublesApi.toBauble(stack1).getModules(stack1, entity));
        }
        this.core.apply(entity);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase entity) {
        if (stack == null || stack.isEmpty()) return false;
        IBauble bauble = BaublesApi.toBauble(stack);
        if (bauble != null) {
            boolean canEquip = bauble.canEquip(stack, entity);
            boolean hasSlot = MODIFIED_SLOTS.get(slot).contains(bauble.getTypes(stack));
            return canEquip && hasSlot;
        }
        return false;
    }

    @Override
    protected void validateSlotIndex(int slot) {
        super.validateSlotIndex(slot);
    }

    @Override public boolean isEventBlocked() { return blockEvents; }
    @Override public void setEventBlock(boolean blockEvents) { this.blockEvents = blockEvents; }
    @Override public boolean isChanged(int slot) { return false; }
    @Override public void setChanged(int slot, boolean change) {}
	@Override public void setPlayer(EntityLivingBase entity) { this.entity = entity; }

    @Override
    public void addListener(IBaublesListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(IBaublesListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public boolean canSync() {
        return !this.syncLock;
    }

    @Override
    public void setVisible(int slot, boolean v) {
        this.vis.markDirty(slot);
        if (v) {
            this.visibility.clear(slot);
        }
        else this.visibility.set(slot);
    }

    @Override
    public boolean getVisible(int slot) {
        return !this.visibility.get(slot);
    }

    @Override
    public EntityLivingBase getOwner() {
        return this.entity;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        // item persistence
        NBTTagList itemList = new NBTTagList();
//        NBTTagList typeList = new NBTTagList();
        for (int i = 0; i < this.stacks.size(); i++) {
            ItemStack itemStack = this.stacks.get(i);
            if (!itemStack.isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                itemStack.writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }

//            BaubleTypeEx type = this.MODIFIED_SLOTS.get(i);
        }
        NBTTagCompound visibility = new NBTTagCompound();
        this.visibility.stream().forEach(i -> visibility.setBoolean(String.valueOf(i), false));

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", itemList);
        nbt.setTag("Visibility", visibility);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.MODIFIED_SLOTS.addAll(AttributeManager.computeSlots(this.entity));
        this.setSize(MODIFIED_SLOTS.size());
        this.snapshot = new ArrayList<>(this.stacks);

        if (nbt.hasKey("Visibility")) {
            NBTTagCompound visibility = nbt.getCompoundTag("Visibility");
            for (String index : visibility.getKeySet()) {
                this.visibility.set(Integer.parseInt(index));
            }
        }

        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTags = itemList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            ItemStack stack = new ItemStack(itemTags);
            if (slot < this.stacks.size() && this.isItemValidForSlot(slot, stack, this.entity)) {
                this.stacks.set(slot, stack);
                this.snapshot.set(slot, stack.copy());
            }
            else {
                this.dropQue.add(stack);
            }
        }
    }

    public void dropItems() {
        while (!dropQue.isEmpty()) {
            dropItem(dropQue.poll());
        }
    }

    private void dropItem(ItemStack stack) {
        if (!entity.world.isRemote) {
            entity.entityDropItem(stack, 0F);
            this.core.batchDecrement(BaublesApi.toBauble(stack).getModules(stack, entity));
            this.core.apply(entity);
        }
        if (BaublesApi.isBauble(stack)) {
            BaublesApi.toBauble(stack).onUnequipped(stack, entity);
        }
    }

    public void onRespawn() {
        this.respawnTask.run();
        this.respawnTask = this::dropItems;
    }

    public void setRespawnTask(Runnable task) {
        this.respawnTask = task;
    }

    public void copyFrom(BaublesContainer that) {
        this.core = that.core;
        this.core.apply(this.entity);

        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(that.MODIFIED_SLOTS);
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(AttributeManager.computeSlots(entity));

        this.stacks = that.stacks;
        this.snapshot = that.snapshot;
        this.visibility = that.visibility;


        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = this.stacks.get(i);
            if (!stack.isEmpty() && BaublesApi.isBauble(stack)) {
                BaublesApi.toBauble(stack).onEquipped(stack, this.entity);
            }
        }

        onSlotChanged();
        dropItems();
    }
}
