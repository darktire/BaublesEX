package baubles.common.container;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesItemHandler;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.BaubleContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class SlotBaubleHandler extends SlotItemHandler {

    private int index;
    private final EntityPlayer player;
    private final BaublesItemHandler baubles;

    public SlotBaubleHandler(EntityPlayer player, IBaublesItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.index = index;
        this.baubles = (BaublesItemHandler)itemHandler;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ((IBaublesItemHandler)getItemHandler()).isItemValidForSlot(index, stack, player);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        ItemStack stack = getStack();
        if (stack.isEmpty()) return false;
        IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        return bauble == null || bauble.canUnequip(stack, player);
    }

    @Override
    public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
        if (!stack.isEmpty() && !((IBaublesItemHandler)getItemHandler()).isEventBlocked()) {
            IBauble bauble = stack.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
            if (bauble != null) bauble.onUnequipped(stack, playerIn);
        }

        this.onSlotChanged();
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        if (getHasStack() && !ItemStack.areItemStacksEqual(stack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
            getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null).onUnequipped(getStack(), player);
        }

        ItemStack oldstack = getStack().copy();

        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.onSlotChanged();

        if (getHasStack() && !ItemStack.areItemStacksEqual(oldstack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && getStack().hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
            Objects.requireNonNull(getStack().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)).onEquipped(getStack(), player);
        }
    }

    @Override
    public String getSlotTexture() {
        return "baubles:gui/slots/"+ BaubleContent.getSlots().get(baubles.setSlot(index));
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public ItemStack getStack() {
        return this.getItemHandler().getStackInSlot(index);
    }

    @Override
    public void onSlotChange(ItemStack itemStack1, ItemStack itemStack2) {
        super.onSlotChange(itemStack1, itemStack2);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return super.decrStackSize(amount);
    }

    @Override
    public IItemHandler getItemHandler() {
        return super.getItemHandler();
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return super.isSameInventory(other);
    }

}