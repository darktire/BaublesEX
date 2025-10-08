package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesListener;
import baubles.common.config.Config;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerPlayerExpanded extends Container implements IBaublesListener<ContainerPlayerExpanded> {
    private final EntityPlayer player;
    private final EntityLivingBase entity;
    public IBaublesItemHandler baubles;
    public int baublesAmount;
    public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public final InventoryCraftResult craftResult = new InventoryCraftResult();
    private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private final BitSet visibility = new BitSet();

    public ContainerPlayerExpanded(EntityPlayer player) {
        this(player, player);
    }

    public ContainerPlayerExpanded(EntityPlayer player, EntityLivingBase entity) {
        this.player = player;
        this.entity = entity;
        this.baubles = BaublesApi.getBaublesHandler(entity);
        this.baublesAmount = this.baubles.getSlots();
        InventoryPlayer playerInv = player.inventory;

        //add craftResult (1) [0,1)
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 154, 28));

        //add craftMatrix (4) [1,5)
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }

        //add armor slots (4) [5,9)
        for (int k = 0; k < 4; k++) {
            final EntityEquipmentSlot slot = equipmentSlots[k];
            this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + k * 18) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem().isValidArmor(stack, slot, player);
                }

                @Override
                public boolean canTakeStack(EntityPlayer playerIn) {
                    ItemStack itemstack = this.getStack();
                    return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
                }

                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
                }
            });
        }

        //add inventory upper half (27) [9,36)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //add inventory downer half (9) [36,45)
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }

        //add offhand slot (1) [45,46)
        this.addSlotToContainer(new Slot(playerInv, 40, 77, 62) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack);
            }

            @Override
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        //add bauble slots (amount)
        if (Config.Gui.widerBar) this.addWideBaubles();
        else this.addSlimBaubles();

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void addSlimBaubles() {
        for (int i = 0; i < this.baublesAmount; i++) {
            this.addSlotToContainer(new SlotBaubleHandler(this.entity, this.baubles, i, -23, 16 + i * 18));
        }
    }

    public void addWideBaubles() {
        for (int i = 0; i < this.baublesAmount; i++) {
            int j = i / Config.Gui.column;
            int k = i % Config.Gui.column;
            this.addSlotToContainer(new SlotBaubleHandler(this.entity, this.baubles, i, -23 + (k - Config.Gui.column + 1) * 18, 16 + j * 18));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        this.slotChangedCraftingGrid(this.player.getEntityWorld(), this.player, this.craftMatrix, this.craftResult);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.baubles.removeListener(this);
        this.craftResult.clear();

        if (!player.world.isRemote) {
            this.clearContainer(player, player.world, this.craftMatrix);
        }
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack oldStack = slot.getStack();
            newStack = oldStack.copy();
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(newStack);
            boolean isMerge = false;

            // craftResult -> inv
            if (index == 0) {
                isMerge = this.mergeItemStack(oldStack, 9, 45, true);
                if (!isMerge) slot.onSlotChange(oldStack, newStack);
            }
            // craftMatrix -> inv
            else if (index < 5) {
                isMerge = this.mergeItemStack(oldStack, 9, 45, false);
            }
            // armor -> inv
            else if (index < 9) {
                isMerge = this.mergeItemStack(oldStack, 9, 45, false);
            }
            // offhand -> inv
            else if (index == 45) {
                isMerge = this.mergeItemStack(oldStack, 9, 45, false);
            }
            // baubles -> inv
            else if (46 <= index && index < 46 + baublesAmount) {
                isMerge = this.mergeItemStack(oldStack, 9, 45, false);
            }
            // inv -> armor
            else if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
                int i = 8 - entityequipmentslot.getIndex();
                isMerge = this.mergeItemStack(oldStack, i, i + 1, false);
            }
            // inv -> offhand
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
                isMerge = this.mergeItemStack(oldStack, 45, 46, false);
            }
            // inv -> bauble
            else if (BaublesApi.isBauble(newStack)) {
                IBauble bauble = BaublesApi.toBauble(newStack);
                isMerge = bauble.canEquip(oldStack, player) && this.mergeItemStack(oldStack, 46, 46 + baublesAmount,false);
            }

            if (!isMerge) {
                // upper -> downer
                if (9 <= index && index < 36 ) {
                    isMerge = this.mergeItemStack(oldStack, 36, 45, false);
                }
                // downer -> upper
                else if (36 <= index && index < 45) {
                    isMerge = this.mergeItemStack(oldStack, 9, 36, false);
                }
                // else?
                else isMerge = this.mergeItemStack(oldStack, 9, 45, false);
            }

            if (!isMerge) return ItemStack.EMPTY;

            if (oldStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.putStack(oldStack);
                slot.onSlotChanged();
            }

            if (oldStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }

            if (oldStack.isEmpty() && !baubles.isEventBlocked() && slot instanceof SlotBaubleHandler) {
                IBauble bauble = BaublesApi.toBauble(newStack);
                if (bauble != null)
                    bauble.onUnequipped(newStack, playerIn);
            }

            ItemStack itemstack2 = slot.onTake(playerIn, oldStack);

            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return newStack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                }
                else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    }
                    else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            }
            else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                }
                else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    }
                    else {
                        slot1.putStack(stack.splitStack(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                }
                else {
                    ++i;
                }
            }
        }

        return flag;
    }

    @Override
    public void setAll(List<ItemStack> list) {
        for (int i = 0; i < list.size(); ++i) {
            Slot slot = this.getSlot(i);
            if (slot instanceof SlotBaubleHandler) ((SlotBaubleHandler) slot).setStack(list.get(i));
            else slot.putStack(list.get(i));
        }
        this.baubles.getDirty().set(0, this.baublesAmount);
    }

    @Override
    public void detectAndSendChanges() {
        for (int i = 0; i < 46; ++i) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                boolean clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack);
                itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                if (clientStackChanged) {
                    for (IContainerListener listener : this.listeners) {
                        listener.sendSlotContents(this, i, itemstack1);
                    }
                }
            }
        }
    }

    public void syncBaubles() {
        Set<EntityPlayer> receivers = null;
        for (int i = 0, slots = this.baubles.getSlots(); i < slots; i++) {
            if (!this.baubles.getDirty().get(i)) continue;

            ItemStack stack = this.baubles.getStackInSlot(i);
            ItemStack stack1 = this.inventoryItemStacks.get(i + 46);

            boolean clientStackChanged = false;
            if (!ItemStack.areItemStacksEqual(stack1, stack)) {
                clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(stack1, stack);
                stack1 = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
                this.inventoryItemStacks.set(i + 46, stack1);
            }

            boolean v = this.baubles.getVisible(i);
            boolean v1 = this.visibility.get(i);

            boolean visibilityChanged = v == v1;
            if (visibilityChanged) {
                if (v) this.visibility.clear(i);
                else this.visibility.set(i);
            }

            if (clientStackChanged || visibilityChanged) {
                if (receivers == null) {
                    receivers = new HashSet<>(((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(this.entity));
                    if (this.entity instanceof EntityPlayer) receivers.add((EntityPlayer) this.entity);
                }
                PacketSync pkt = PacketSync.severPack(this.entity, i, stack1, v);
                for (EntityPlayer receiver : receivers) {
                    PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP) receiver);
                }
            }

            this.baubles.getDirty().clear(i);
        }
    }

    @Override
    public void updateBaubles() {
        this.clearBaubles();
        this.baublesAmount = this.baubles.getSlots();
        if (!this.entity.world.isRemote) {
            this.addSlimBaubles();
        }
    }

    @Override
    public ContainerPlayerExpanded startListening() {
        this.baubles.addListener(this);
        return this;
    }

    public void clearBaubles() {
        this.inventorySlots.subList(46, 46 + this.baublesAmount).clear();
        this.inventoryItemStacks.subList(46, 46 + this.baublesAmount).clear();
    }
}