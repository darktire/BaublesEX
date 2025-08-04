package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesModifiable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerPlayerExpanded extends Container {
    public IBaublesModifiable baubles;
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
        this.baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);

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
        this.addBaubleSlots(player);

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void removeBaubleSlots() {
        this.inventorySlots.subList(46, 46 + slotsAmount).clear();
        this.inventoryItemStacks.subList(46, 46 + slotsAmount).clear();
    }

    public void addBaubleSlots(EntityPlayer player) {
        this.slotsAmount = baubles.getSlots();
        for (int i = 0; i < slotsAmount; i++) {
            this.addSlotToContainer(new SlotBaubleHandler(player, baubles, i, -23, 16 + (i * 18)));
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
            else if (46 <= index && index < 46 + slotsAmount) {
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
                isMerge = bauble.canEquip(oldStack, player) && this.mergeItemStack(oldStack, 46, 46 + slotsAmount,false);
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

    //private void unequipBauble(ItemStack stack) { }
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
    }
}