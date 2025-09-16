package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.registries.TypesData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Predicate;

public class BaublesContainer extends ItemStackHandler implements IBaublesModifiable {

    private boolean blockEvents = false;
    private static final BaubleTypeEx TRINKET = TypesData.Preset.TRINKET;
    private EntityLivingBase entity;
    private final Deque<ItemStack> droppingItem = new ArrayDeque<>();

    private final List<BaubleTypeEx> MODIFIED_SLOTS = new ArrayList<>();
    private final List<BaubleTypeEx> PREVIOUS_SLOTS = new ArrayList<>();
//    private final HashMap<String, Integer> MODIFIER_FACTOR = new HashMap<>();
    private final Map<String, Integer> BAUBLE_MODIFIER = new HashMap<>();
//    private final List<Integer> ACTIVE_SLOTS = new ArrayList<>();
    private final Map<Integer, Boolean> visibility = new HashMap<>();

    public static final List<BaublesContainer> CONTAINERS = new ArrayList<>();
    private final List<WeakReference<IBaublesListener<?>>> listeners = new ArrayList<>();
    public boolean containerUpdated = true;

    public BaublesContainer() { super(TypesData.getSum()); }

    public BaublesContainer(EntityLivingBase entity) {
        super(TypesData.getSum());
        this.entity = entity;
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        CONTAINERS.add(this);
    }

    @Override
    public void setSlot(String typeName, int n) {
        BaubleTypeEx type = TypesData.getTypeByName(typeName);
        if (type != null) {
            int original = type.getAmount();
            modifySlotOA(typeName, n - original);
        }
    }

    @Override
    public void modifySlot(String typeName, int modifier) {
        int original = this.BAUBLE_MODIFIER.getOrDefault(typeName, 0);
        modifySlotOA(typeName, modifier - original);
    }

    @Override
    public void modifySlotOA(String typeName, int modifier) {
        if (modifier != 0) {
            this.storeSlots();
            int input = this.modifySlotOL(typeName, modifier);
            if (input != 0) {
                this.BAUBLE_MODIFIER.put(typeName, input);
            }
            else {
                this.BAUBLE_MODIFIER.remove(typeName);
            }
            if (this.containerUpdated) this.containerUpdated = false;
        }
    }

    /**
     * Only modify slots without storing previous slots or updating {@link BaublesContainer#BAUBLE_MODIFIER}.
     * Also will not trigger {@link BaublesContainer#updateContainer()}.
     * @return current modifier
     */
    private int modifySlotOL(String typeName, int modifier) {
        BaubleTypeEx type = TypesData.getTypeByName(typeName);
        if (type == null) throw new RuntimeException("No such bauble type");
        int original = this.BAUBLE_MODIFIER.getOrDefault(typeName, 0);
        if (modifier > 0) {
            int index = this.MODIFIED_SLOTS.indexOf(type);
            if (index == -1) {
                for (int i = 1;; i++) {
                    int id = TypesData.getId(type);
                    BaubleTypeEx previous = TypesData.getTypeById(id - i);
                    index = this.MODIFIED_SLOTS.lastIndexOf(previous) + 1;
                    if (index != 0) break;
                    if (id - i == 0) break;
                }
            }
            for (int i = 0; i < modifier; i++) {
                this.MODIFIED_SLOTS.add(index, type);
            }
        }
        else {
            int lastIndex = this.MODIFIED_SLOTS.lastIndexOf(type);
            if (lastIndex == -1) return original;
            if (modifier + type.getAmount() + original < 0) modifier = -type.getAmount() - original;
            this.MODIFIED_SLOTS.subList(lastIndex + modifier + 1, lastIndex + 1).clear();
        }

        return original + modifier;
    }

    @Override
    public void clearModifier() {
        this.storeSlots();
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        this.BAUBLE_MODIFIER.clear();
        this.containerUpdated = false;
    }

    @Override
    public void updateContainer() {
        if (!this.containerUpdated) {
            this.onSlotChanged();
            this.listeners.removeIf(ref -> {
                IBaublesListener<?> listener = ref.get();
                if (listener == null) return true;
                listener.updateBaubles();
                return false;
            });
        }
    }

    private void storeSlots() {
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
    }

    private void onSlotChanged() {
        List<ItemStack> stacks1 = new ArrayList<>(this.stacks);
        Map<Integer, Boolean> visibility1 = new HashMap<>(this.visibility);
        setSize(this.MODIFIED_SLOTS.size());
        this.visibility.clear();
        boolean drop;
        for (int i = 0; i < stacks1.size(); i++) {
            boolean flag = visibility1.getOrDefault(i, true);
            ItemStack stack = stacks1.get(i);
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
                    if (!empty) this.stacks.set(newIndex, stack);
                    if (!flag) this.visibility.put(newIndex, false);
                }
                else drop = true;
            }
            if (drop) {
                if (!this.entity.world.isRemote) this.entity.entityDropItem(stack, 0);
                BaublesApi.toBauble(stack).onUnequipped(stack, this.entity);
            }
        }
        if (!this.containerUpdated) this.containerUpdated = true;
    }

    public void onConfigChanged() {
        this.storeSlots();
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        this.BAUBLE_MODIFIER.forEach(this::modifySlotOL);
        this.containerUpdated = false;
        this.updateContainer();
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
    public int indexOf(Object o, int start) {
        if (o instanceof ItemStack) return indexOf(this.stacks, start, stack -> ItemStack.areItemStacksEqual(stack, (ItemStack) o));
        else if (o instanceof Item) return indexOf(this.stacks, start, stack -> stack.getItem() == o);
        else if (o instanceof BaubleTypeEx) return indexOf(this.MODIFIED_SLOTS, start, type -> type == o);
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

//    @Override
//    public LinkedList<Integer> getValidSlots(BaubleTypeEx type) {
//        LinkedList<Integer> list = new LinkedList<>();
//        if (type == TRINKET) {
//            for (int i = 0; i < MODIFIED_SLOTS.size(); i++) list.add(i);
//        }
//        else {
//            int index = MODIFIED_SLOTS.indexOf(type);
//            while (index != -1) {
//                if (MODIFIED_SLOTS.subList(index, MODIFIED_SLOTS.size()).contains(type)) list.add(index++);
//                else break;
//            }
//        }
//        return list;
//    }

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
            boolean hasSlot = bauble.getTypes(stack).contains(MODIFIED_SLOTS.get(slot)) || bauble.getTypes(stack).contains(TRINKET) || MODIFIED_SLOTS.get(slot) == TRINKET;
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
        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
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
        return false;
    }

    @Override
    public void setChanged(int slot, boolean change) {}

	@Override
	public void setPlayer(EntityLivingBase entity) {
		this.entity = entity;
	}

    @Override
    public void addListener(IBaublesListener<?> listener) {
        this.listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(IBaublesListener<?> listener) {
        this.listeners.removeIf(ref -> ref.get() == listener);
    }

    @Override
    public boolean haveDroppingItem() {
        return !this.droppingItem.isEmpty();
    }

    @Override
    public ItemStack getDroppingItem() {
        return this.droppingItem.pop();
    }

    @Override
    public void setVisible(int slot, boolean v) {
        this.visibility.put(slot, v);
    }

    @Override
    public boolean getVisible(int slot) {
        return this.visibility.getOrDefault(slot, true);
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
        this.BAUBLE_MODIFIER.forEach(modifier::setInteger);
        NBTTagCompound visibility = new NBTTagCompound();
        this.visibility.forEach((i, v) -> {
            if (!v) {
                visibility.setBoolean(i.toString(), false);
            }
        });


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
            for (String typeName : modifier.getKeySet()) {
                int input = modifier.getInteger(typeName);
                this.modifySlotOL(typeName, input);
                this.BAUBLE_MODIFIER.put(typeName, input);
            }
            this.storeSlots();
            this.setSize(MODIFIED_SLOTS.size());
        }

        if (nbt.hasKey("Visibility")) {
            NBTTagCompound visibility = nbt.getCompoundTag("Visibility");
            for (String index : visibility.getKeySet()) {
                this.visibility.put(Integer.valueOf(index), visibility.getBoolean(index));
            }
        }

        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
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
            }
            else {
                this.droppingItem.push(stack);
            }
        }
    }
}
