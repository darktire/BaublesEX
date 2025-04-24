package baubles.common.container;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerPlayerExpanded extends Container {
    public IBaublesItemHandler baubles;
    private int slotsAmount;
    /**
     * The crafting matrix inventory.
     */
    public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public final InventoryCraftResult craftResult = new InventoryCraftResult();
    /**
     * Determines if inventory manipulation should be handled.
     */
    public boolean isLocalWorld;
    private final EntityPlayer player;
    private static final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    public ContainerPlayerExpanded(InventoryPlayer playerInv, boolean world, EntityPlayer player) {
        this.isLocalWorld = world;
        this.player = player;
        baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        ((BaublesContainer) baubles).updateSlots(player);
        this.slotsAmount = baubles.getSlots();

        //add craftResult (1)
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 154, 28));

        //add craftMatrix (4)
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
            }
        }

        //add armor slots (4)
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

        //add bauble slots (amount)
        for (int i = 0; i < slotsAmount; i++) {
            this.addSlotToContainer(new SlotBaubleHandler(player, baubles, i, -21, 15 + (i * 18)));
        }

        //add inventory upper half (27)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //add inventory downer half (9)
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }

        //add offhand slot (1)
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
        this.onCraftMatrixChanged(this.craftMatrix);
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
            int slotShift = slotsAmount;
            boolean isMerge = false;

            // craftResult -> inv
            if (index == 0) {
                isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 45 + slotShift, true);
                if (!isMerge) slot.onSlotChange(oldStack, newStack);
            }
            // craftMatrix -> inv
            else if (index < 5) {
                isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 45 + slotShift, false);
            }
            // armor -> inv
            else if (index < 9) {
                isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 45 + slotShift, false);
            }
            // baubles -> inv
            else if (index < 9 + slotShift) {
                isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 45 + slotShift, false);
            }
            // inv -> armor
            else if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
                int i = 8 - entityequipmentslot.getIndex();
                isMerge = this.mergeItemStack(oldStack, i, i + 1, false);
            }
            // inv -> offhand
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45 + slotShift).getHasStack()) {
                isMerge = this.mergeItemStack(oldStack, 45 + slotShift, 46 + slotShift, false);
            }
            // inv -> bauble
            else if (newStack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
                IBauble bauble = newStack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                isMerge = bauble.canEquip(oldStack, player) && this.mergeItemStack(oldStack, 9, 8 + slotShift,false);
            }
            if (!isMerge) {
                // upper -> downer
                if (index >= 9 + slotShift && index < 36 + slotShift) {
                    isMerge = this.mergeItemStack(oldStack, 36 + slotShift, 45 + slotShift, false);
                }
                // downer -> upper
                else if (index >= 36 + slotShift && index < 45 + slotShift) {
                    isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 36 + slotShift, false);
                }
                // else
                else isMerge = this.mergeItemStack(oldStack, 9 + slotShift, 45 + slotShift, false);
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
                IBauble cap = newStack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                if (cap != null)
                    cap.onUnequipped(newStack, playerIn);
            }

            ItemStack itemstack2 = slot.onTake(playerIn, oldStack);

            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return newStack;
    }

    //private void unequipBauble(ItemStack stack) { }
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
    }
}