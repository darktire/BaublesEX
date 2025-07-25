package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.Baubles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBaubleHandler extends SlotItemHandler {

    private int index;
    private final EntityPlayer player;

    public SlotBaubleHandler(EntityPlayer player, IBaublesItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.index = index;
    }

    public void incrYPos(int value) {
        super.yPos += value;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ((IBaublesItemHandler)getItemHandler()).isItemValidForSlot(index, stack, player);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        ItemStack stack = getStack();
        if (stack.isEmpty()) return false;
        IBauble bauble = BaublesApi.getBaubleItem(stack);
        return bauble == null || player.isCreative() || bauble.canUnequip(stack, player);
    }

    @Override
    public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
        if (!stack.isEmpty() && !((IBaublesItemHandler)getItemHandler()).isEventBlocked()) {
            IBauble bauble = BaublesApi.getBaubleItem(stack);
            if (bauble != null) bauble.onUnequipped(stack, playerIn);
        }

        this.onSlotChanged();
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        if (getHasStack() && !ItemStack.areItemStacksEqual(stack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && BaublesApi.isBauble(getStack())) {
            BaublesApi.getBaubleItem(getStack()).onUnequipped(getStack(), player);
        }

        ItemStack oldStack = getStack().copy();

        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.onSlotChanged();//no effect

        if (getHasStack() && !ItemStack.areItemStacksEqual(oldStack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked()) {
            BaublesApi.getBaubleItem(getStack()).onEquipped(getStack(), player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEnabled()
    {
        return -2 < yPos && yPos <= 142;
    }//visible

    @Override
    public String getSlotTexture() {
        return "baubles:"+ Baubles.baubles.getSlot(index).getTexture();
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