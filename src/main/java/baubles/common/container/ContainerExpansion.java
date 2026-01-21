package baubles.common.container;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.cap.IBaublesListener;
import baubles.common.config.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ContainerExpansion extends Container implements IBaublesListener {
    protected EntityLivingBase entity;
    public IBaublesItemHandler baubles;
    public int baublesAmount;

    public ContainerExpansion(EntityLivingBase entity) {
        this.entity = entity;
        this.baubles = BaublesApi.getBaublesHandler(entity);
        if (!entity.world.isRemote) this.baubles.addListener(this);

        initSlots();
    }

    protected void initSlots() {
        //add offhand slot (1) [45,46)
//        this.addSlotToContainer(new Slot(playerInv, 40, 77, 62) {
//            @Override
//            public boolean isItemValid(ItemStack stack) {
//                return super.isItemValid(stack);
//            }
//
//            @Override
//            public String getSlotTexture() {
//                return "minecraft:items/empty_armor_slot_shield";
//            }
//        });

        //add bauble slots (amount)
        addBaubleSlots(Config.Gui.widerBar);
    }

    public void addBaubleSlots(boolean flag) {
        int column = flag ? Config.Gui.column : 1;
        this.baublesAmount = this.baubles.getSlots();
        for (int i = 0; i < this.baublesAmount; i++) {
            int j = i / column;
            int k = i % column;
            this.addSlotToContainer(new SlotBaubleHandler(this.entity, this.baubles, i, -23 + (k - column + 1) * 18, 16 + j * 18));
        }
    }

    public List<Slot> getBaubleSlots() {
        return this.inventorySlots;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.baubles.removeListener(this);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return false;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setAll(List<ItemStack> list) {
        for (int i = 0; i < list.size(); ++i) {
            ((SlotBaubleHandler) this.getSlot(i)).setStack(list.get(i));
        }
    }

    @Override
    public void detectAndSendChanges() {}

    @Override
    public void syncChanges() {
        this.clearBaubles();
        this.addBaubleSlots(false);
    }

    public void clearBaubles() {
        this.getBaubleSlots().clear();
        this.getBaubleStacks().clear();
    }

    public List<ItemStack> getBaubleStacks() {
        return this.inventoryItemStacks;
    }
}
