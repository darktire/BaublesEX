package baubles.api.cap;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.registries.TypesData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BaublesContainer extends ItemStackHandler implements IBaublesModifiable {

    private boolean blockEvents = false;
    private static final BaubleTypeEx TRINKET = TypesData.getTypeByName("trinket");
    private EntityLivingBase entity;
    private final ArrayList<BaubleTypeEx> MODIFIED_SLOTS = new ArrayList<>();
    private final ArrayList<BaubleTypeEx> PREVIOUS_SLOTS = new ArrayList<>();
//    private final HashMap<String, Integer> MODIFIER_FACTOR = new HashMap<>();
    private final HashMap<String, Integer> BAUBLE_MODIFIER = new HashMap<>();

    public static final HashSet<BaublesContainer> listener = new HashSet<>();
    public boolean slotsUpdated = true;
    public boolean guiUpdated = true;

    public BaublesContainer() { super(TypesData.getSum()); }

    public BaublesContainer(EntityLivingBase entity) {
        super(TypesData.getSum());
        this.entity = entity;
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        listener.add(this);
    }

    @Override
    public void modifySlot(String typeName, int modifier) {
        int original = this.BAUBLE_MODIFIER.getOrDefault(typeName, 0);
        modifySlotOA(typeName, modifier - original);
    }

    @Override
    public void modifySlotOA(String typeName, int modifier) {
        if (modifier == 0) return;
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
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
            if (lastIndex == -1) return;
            if (modifier + type.getAmount() + original < 0) modifier = -type.getAmount() - original;
            this.MODIFIED_SLOTS.subList(lastIndex + modifier + 1, lastIndex + 1).clear();
        }

        this.BAUBLE_MODIFIER.put(typeName, original + modifier);
        if (this.slotsUpdated) this.slotsUpdated = false;
    }

    @Override
    public void clearModifier() {
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        this.BAUBLE_MODIFIER.clear();
        this.slotsUpdated = false;
    }

    @Override
    public void updateSlots() {
        if (!this.slotsUpdated) onSlotChanged();
    }

    private void onSlotChanged() {
        NonNullList<ItemStack> stacks1 = this.stacks;
        setSize(this.MODIFIED_SLOTS.size());
        int move1 = 0;
        boolean drop;
        for (int i = 0; i < stacks1.size(); i++) {
            ItemStack stack = stacks1.get(i);
            if (stack.isEmpty()) continue;
            IBauble bauble = BaublesApi.toBauble(stack);
            BaubleTypeEx type = bauble.getBaubleTypeEx();
            if (type == TRINKET) {
                int newIndex = i + move1;
                if (newIndex < this.MODIFIED_SLOTS.size()) {
                    this.stacks.set(newIndex, stack);
                    continue;
                }
            }
            int start = this.MODIFIED_SLOTS.indexOf(type);
            drop = false;
            if (start == -1) drop = true;
            else {
                int move = start - this.PREVIOUS_SLOTS.indexOf(type);
                int newIndex = i + move;
                if (newIndex < this.MODIFIED_SLOTS.size() && this.MODIFIED_SLOTS.get(newIndex) == type) {
                    this.stacks.set(newIndex, stack);
                    move1 = move;
                }
                else drop = true;
            }
            if (drop && !this.entity.world.isRemote) {
                this.entity.entityDropItem(stack, 0);
                bauble.onUnequipped(stack, this.entity);
            }
        }
        if (!this.slotsUpdated) this.slotsUpdated = true;
        if (this.guiUpdated) this.guiUpdated = false;
    }

    public void onConfigChanged() {
        this.PREVIOUS_SLOTS.clear();
        this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
        this.MODIFIED_SLOTS.clear();
        this.MODIFIED_SLOTS.addAll(TypesData.getLazyList());
        for (String typeName: this.BAUBLE_MODIFIER.keySet()) {
            modifySlotOA(typeName, this.BAUBLE_MODIFIER.get(typeName));
        }
        this.slotsUpdated = false;
    }

    @Override
    public int getModifier(String typeName) {
        return BAUBLE_MODIFIER.getOrDefault(typeName, 0);
    }

    @Override
    public BaubleTypeEx getTypeInSlot(int slot) {
        return MODIFIED_SLOTS.get(slot);
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
            boolean hasSlot = bauble.getBaubleTypeEx() == MODIFIED_SLOTS.get(slot) || bauble.getBaubleTypeEx() == TRINKET;
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
            this.PREVIOUS_SLOTS.clear();
            this.PREVIOUS_SLOTS.addAll(this.MODIFIED_SLOTS);
            this.slotsUpdated = true;
        }

        NBTTagList itemList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTags = itemList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(slot, new ItemStack(itemTags));
            }
            else {
                this.entity.entityDropItem(new ItemStack(itemTags), 0);
            }
        }
    }
}
