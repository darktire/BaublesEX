package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.event.BaublesChangeEvent;
import baubles.api.event.BaublesValidationEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBaubleHandler extends SlotItemHandler {

    private final int index;
    private final int startYPos;
    private final EntityLivingBase entity;
    protected boolean locked = false;

    public SlotBaubleHandler(EntityLivingBase entity, IBaublesItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.entity = entity;
        this.index = index;
        this.startYPos = yPosition;
    }

    @SideOnly(Side.CLIENT)
    public void setYPos(int value) {
        this.yPos = this.startYPos + value;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        BaublesValidationEvent event = new BaublesValidationEvent(this.entity, stack, getItemHandler().isItemValidForSlot(index, stack, this.entity));
        MinecraftForge.EVENT_BUS.post(event);
        return event.getRet();
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
        BaublesChangeEvent event = new BaublesChangeEvent(this.entity, this.getItemHandler().isEventBlocked(), this.index, stack, ItemStack.EMPTY);
        MinecraftForge.EVENT_BUS.post(event);

        if (!stack.isEmpty() && !event.isBlocked()) {
            IBauble bauble = BaublesApi.toBauble(stack);
            if (bauble != null) bauble.onUnequipped(stack, this.entity);
            this.onSlotChanged();
        }

        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
        BaublesChangeEvent event = new BaublesChangeEvent(this.entity, this.getItemHandler().isEventBlocked(), this.index, getStack(), stack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isBlocked()) return;

        ItemStack stack1 = getStack();

        this.getItemHandler().setStackInSlot(index, stack.copy());
        this.onSlotChanged();

        if (!ItemStack.areItemStacksEqual(stack, stack1)) {
            if (BaublesApi.isBauble(stack1)) {
                BaublesApi.toBauble(stack1).onUnequipped(stack1, this.entity);
            }
            if (BaublesApi.isBauble(stack)) {
                BaublesApi.toBauble(stack).onEquipped(stack, this.entity);
            }
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
        return this.getItemHandler().getTypeInSlot(index).getTexture();
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
    public IBaublesItemHandler getItemHandler() {
        return (IBaublesItemHandler) super.getItemHandler();
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return super.isSameInventory(other);
    }

    public void setLocked(boolean lock) {
        this.locked = lock;
    }
}
//todo lock