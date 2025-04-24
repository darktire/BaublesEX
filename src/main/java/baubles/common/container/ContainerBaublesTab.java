package baubles.common.container;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static baubles.api.BaublesRegister.getSum;

public class ContainerBaublesTab extends Container {
    private final IBaublesItemHandler baubles;
    private final EntityPlayer player;

    public ContainerBaublesTab(InventoryPlayer playerInv, boolean world, EntityPlayer player) {
        baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
        this.player = player;

        //add bauble slots (amount)
        outerLoop:
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (j + i * 9 >= getSum()) {
                    //add offhand slot (1)
                    this.addSlotToContainer(new Slot(playerInv, 40, 8 + j * 18, 18 + i * 18) {
                        @Override
                        public boolean isItemValid(ItemStack stack) {
                            return super.isItemValid(stack);
                        }

                        @Override
                        public String getSlotTexture() {
                            return "minecraft:items/empty_armor_slot_shield";
                        }
                    });
                    break outerLoop;
                }
                this.addSlotToContainer(new SlotBaubleHandler(player, baubles, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
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
    }


    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack oldStack = slot.getStack();
            newStack = oldStack.copy();
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(newStack);
            int slotShift = baubles.getSlots();
            boolean isMerge = false;

            // baubles -> inv
            if (index < slotShift) {
                 isMerge = this.mergeItemStack(oldStack, 1 + slotShift, 37 + slotShift, false);
            }
            // inv -> offhand
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(1 + slotShift).getHasStack()) {
                isMerge = this.mergeItemStack(oldStack, slotShift, 1 + slotShift, false);
            }
            // inv -> bauble
            else if (newStack.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
                IBauble bauble = newStack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                isMerge = bauble.canEquip(oldStack, player) && this.mergeItemStack(oldStack, 0, slotShift,false);
            }

            if (!isMerge) {
                // upper -> downer
                if (index < 28 + slotShift) {
                    isMerge = this.mergeItemStack(oldStack, 28 + slotShift, 37 + slotShift, false);
                }
                // downer -> upper
                else if (index < 37 + slotShift) {
                    isMerge = this.mergeItemStack(oldStack, 1 + slotShift, 28 + slotShift, false);
                }
                // else
                else {
                    isMerge = this.mergeItemStack(oldStack, 1 + slotShift, 37 + slotShift, false);
                }
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
        }

        return newStack;
    }
}
