package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.event.BaublesChangeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBaubleHandler extends SlotItemHandler {

    private final int index;
    private final int startYPos;
    private final EntityLivingBase entity;

    public SlotBaubleHandler(EntityLivingBase entity, IBaublesItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.entity = entity;
        this.index = index;
        this.startYPos = yPosition;
    }

    public void setYPos(int value) {
        this.yPos = this.startYPos + value;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ((IBaublesItemHandler)getItemHandler()).isItemValidForSlot(index, stack, this.entity);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        ItemStack stack = getStack();
        if (stack.isEmpty()) return false;
        IBauble bauble = BaublesApi.toBauble(stack);
        return bauble == null || player.isCreative() || bauble.canUnequip(stack, player);
    }

    @Override
    public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
        BaublesChangeEvent event = new BaublesChangeEvent(this.entity, this.index, stack, ItemStack.EMPTY);
        MinecraftForge.EVENT_BUS.post(event);

        if (!stack.isEmpty() && !((IBaublesItemHandler)getItemHandler()).isEventBlocked()) {
            IBauble bauble = BaublesApi.toBauble(stack);
            if (bauble != null) bauble.onUnequipped(stack, this.entity);
        }

        this.onSlotChanged();
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        MinecraftForge.EVENT_BUS.post(new BaublesChangeEvent(this.entity, this.index, ItemStack.EMPTY, stack));

        if (getHasStack() && !ItemStack.areItemStacksEqual(stack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked() && BaublesApi.isBauble(getStack())) {
            BaublesApi.toBauble(getStack()).onUnequipped(getStack(), this.entity);
        }

        ItemStack oldStack = getStack().copy();

        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.onSlotChanged();//no effect

        if (getHasStack() && !ItemStack.areItemStacksEqual(oldStack, getStack()) && !((IBaublesItemHandler) getItemHandler()).isEventBlocked()) {
            BaublesApi.toBauble(getStack()).onEquipped(getStack(), this.entity);
        }
    }

    public void setStack(ItemStack stack) {
        super.putStack(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEnabled() {
        return -2 < yPos && yPos <= 142;
    }//visible

    @Override
    public String getSlotTexture() {
        return "baubles:"+ BaublesApi.getBaublesHandler(entity).getTypeInSlot(index).getTexture();
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