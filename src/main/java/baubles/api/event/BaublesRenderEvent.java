package baubles.api.event;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BaublesRenderEvent extends PlayerEvent {
    private final boolean slim;
    private final ItemStack stack;
    private final boolean inBaubles;
    private final int slot;
    private final EntityEquipmentSlot slotIn;

    public BaublesRenderEvent(EntityPlayer player, boolean slim, ItemStack stack, boolean inBaubles, int slot) {
        super(player);
        this.slim = slim;
        this.stack = stack;
        this.inBaubles = inBaubles;
        this.slot = slot;
        this.slotIn = null;
    }

    public BaublesRenderEvent(EntityPlayer player, boolean slim, ItemStack stack, boolean inBaubles, EntityEquipmentSlot slotIn) {
        super(player);
        this.slim = slim;
        this.stack = stack;
        this.inBaubles = inBaubles;
        this.slot = -1;
        this.slotIn = slotIn;
    }

    public boolean isCancelable()
    {
        return true;
    }

    public void setCanceled() {
        this.setCanceled(true);
    }

    public boolean isSlim() {
        return this.slim;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean inBaubleSlots() {
        return this.inBaubles;
    }

    public int getSlot() {
        return this.slot;
    }

    public EntityEquipmentSlot getSlotIn() {
        return this.slotIn;
    }
}
