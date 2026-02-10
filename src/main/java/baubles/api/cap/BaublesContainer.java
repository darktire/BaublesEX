package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.attribute.AdvancedInstance;
import baubles.api.attribute.AttributeManager;
import baubles.api.module.ModuleCore;
import baubles.api.registries.TypeData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
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

    private final Set<IBaublesListener> listeners = Collections.newSetFromMap(new WeakHashMap<>());
    public boolean containerUpdated = true;

    public BaublesContainer() { super(0); }

    public BaublesContainer(EntityLivingBase entity) {
        this();
        this.entity = entity;
        AttributeManager.attachAttributes(entity, this);
        this.MODIFIED_SLOTS.addAll(AttributeManager.computeSlots(entity));
        this.setSize(MODIFIED_SLOTS.size());
        this.snapshot = new ArrayList<>(this.stacks);
    }

    @Override
    public void updateContainer() {
        if (!this.containerUpdated) {
            this.syncSlots();
            this.onSlotChanged();
            if (!this.entity.world.isRemote) {
                this.stx.status.set(0, this.getSlots());
            }
            this.listeners.forEach(IBaublesListener::syncChanges);
        }
    }

    private void syncSlots() {
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(AttributeManager.computeSlots(entity));

    }

    private void onSlotChanged() {
        BitSet visibility1 = (BitSet) this.visibility.clone();
        setSize(this.MODIFIED_SLOTS.size());
        List<ItemStack> temp = new ArrayList<>(this.stacks);
        this.visibility.clear();
        boolean drop;
        List<ItemStack> dropList = new ArrayList<>();
        for (int i = 0; i < this.snapshot.size(); i++) {
            boolean flag = !visibility1.get(i);
            ItemStack stack = this.snapshot.get(i);
            boolean empty = stack.isEmpty();
            if (empty && flag) continue;
            BaubleTypeEx type = this.PREVIOUS_SLOTS.get(i);
            int start = this.MODIFIED_SLOTS.indexOf(type);
            drop = false;
            if (start == -1) drop = true;
            else {
                int move = start - this.PREVIOUS_SLOTS.indexOf(type);
                int newIndex = i + move;
                if (newIndex < this.MODIFIED_SLOTS.size() && this.MODIFIED_SLOTS.get(newIndex) == type) {
                    if (!empty) {
                        this.stacks.set(newIndex, stack);
                        temp.set(newIndex, stack.copy());
                    }
                    if (!flag) this.visibility.set(newIndex);
                }
                else drop = true;
            }

            if (drop) dropList.add(stack);
        }
        this.snapshot = temp;
        this.containerUpdated = true;

        for (ItemStack stack : dropList) {
            if (!this.entity.world.isRemote) {
                this.entity.entityDropItem(stack, 0);
                this.core.batchDecrement(BaublesApi.toBauble(stack).getModules(stack, entity));
                this.core.apply(entity);
            }
            BaublesApi.toBauble(stack).onUnequipped(stack, this.entity);
        }
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
    protected void onContentsChanged(int slot) {
        if (!this.entity.world.isRemote) {
            ItemStack stack = this.snapshot.get(slot);
            ItemStack stack1 = this.stacks.get(slot);
            if (!ItemStack.areItemStacksEqual(stack, stack1)) {
                this.stx.status.set(slot);
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
    public void setVisible(int slot, boolean v) {
        this.vis.status.set(slot);
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
        // modifier persistence
        NBTTagCompound modifier = new NBTTagCompound();
        for (BaubleTypeEx type : TypeData.sortedList()) {
            AdvancedInstance instance = AttributeManager.getInstance(this.entity.getAttributeMap(), type);
            int[] intArr = {
                    (int) instance.getAnonymousModifier(0),
                    (int) instance.getAnonymousModifier(1),
                    (int) instance.getAnonymousModifier(2)
            };
            if (!Arrays.stream(intArr).allMatch(num -> num == 0)) {
                modifier.setIntArray(type.getName(), intArr);
            }
        }
        NBTTagCompound visibility = new NBTTagCompound();
        this.visibility.stream().forEach(i -> visibility.setBoolean(String.valueOf(i), false));


        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", itemList);
        nbt.setTag("Modifier", modifier);
        nbt.setTag("Visibility", visibility);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Modifier")) {
            NBTTagCompound modifier = nbt.getCompoundTag("Modifier");
            AbstractAttributeMap map = this.entity.getAttributeMap();
            for (String typeName : modifier.getKeySet()) {
                BaubleTypeEx type = TypeData.getTypeByName(typeName);
                if (type != null) {
                    NBTBase base = modifier.getTag(typeName);
                    if (base instanceof NBTTagIntArray) {
                        int[] input = ((NBTTagIntArray) base).getIntArray();
                        for (int i = 0; i < 3; i++) {
                            if (input[i] != 0) {
                                AttributeManager.getInstance(map, type).applyAnonymousModifier(i, input[i]);
                            }
                        }
                    }
                    else if (base instanceof NBTPrimitive) {// forward compat
                        int input = ((NBTPrimitive) base).getInt();
                        AttributeManager.getInstance(map, type).applyAnonymousModifier(0, input);
                    }
                }
            }
            this.setSize(MODIFIED_SLOTS.size());
            this.snapshot = new ArrayList<>(this.stacks);
        }

        if (nbt.hasKey("Visibility")) {
            NBTTagCompound visibility = nbt.getCompoundTag("Visibility");
            for (String index : visibility.getKeySet()) {
                this.visibility.set(Integer.parseInt(index));
            }
        }

        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        List<ItemStack> dropList = new ArrayList<>();
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTags = itemList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            boolean flag = false;
            ItemStack stack = new ItemStack(itemTags);
            if (slot >= 0 && slot < this.stacks.size() && this.isItemValidForSlot(slot, stack, this.entity)) {
                flag = true;
            }
            if (flag) {
                this.stacks.set(slot, stack);
                this.snapshot.set(slot, stack.copy());
            }
            else {
                dropList.add(stack);
            }
        }
        dropList.forEach(s -> dropItem(this.entity, s));
    }

    private static void dropItem(EntityLivingBase entity, ItemStack stack) {
        EntityItem ei = entity.entityDropItem(stack, 0F);
        if (ei != null) {
            ei.setNoDespawn();
            ei.setNoPickupDelay();
//            boolean ok = entity.world.spawnEntity(ei);
//            Baubles.log.warn("spawnEntity result = " + ok);
        }
        if (BaublesApi.isBauble(stack)) {
            BaublesApi.toBauble(stack).onUnequipped(stack, entity);
        }
    }

    public void inherit(BaublesContainer that) {
        this.stacks = that.stacks;
        this.snapshot = that.snapshot;
        this.visibility = that.visibility;
        this.core = that.core;

        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = this.stacks.get(i);
            if (!stack.isEmpty() && BaublesApi.isBauble(stack)) {
                BaublesApi.toBauble(stack).onEquipped(stack, this.entity);
            }
        }
        this.core.apply(this.entity);
    }
}
