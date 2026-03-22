package baubles.common.event;

import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class BaubleDropsEvent extends LivingEvent {
    private final IBaublesItemHandler baubles;
    private final ItemStack stack;
    private final int slot;

    public BaubleDropsEvent(EntityLivingBase entity, IBaublesItemHandler baubles, ItemStack stack, int slot) {
        super(entity);
        this.baubles = baubles;
        this.stack = stack;
        this.slot = slot;
    }

    public IBaublesItemHandler getBaubles() {
        return baubles;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getSlot() {
        return slot;
    }

    public void noDrops() {
        setCanceled(true);
    }
}
