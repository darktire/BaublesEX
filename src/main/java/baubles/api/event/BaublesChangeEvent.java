package baubles.api.event;

import baubles.api.cap.IBaublesModifiable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BaublesChangeEvent extends LivingEvent {
    private final EntityLivingBase target;
    private final IBaublesModifiable baubles;
    private final int index;
    private final ItemStack stackOut;
    private final ItemStack stackIn;
    private boolean isBlocked;

    public BaublesChangeEvent(EntityLivingBase target, IBaublesModifiable baubles, int index, ItemStack out, ItemStack in) {
        super(target);
        this.target = target;
        this.baubles = baubles;
        this.index = index;
        this.stackOut = out;
        this.stackIn = in;
        this.isBlocked = baubles.isEventBlocked();
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }

    public IBaublesModifiable getBaubles() {
        return this.baubles;
    }

    public int getIndex() {
        return this.index;
    }

    public ItemStack getStackOut() {
        return this.stackOut;
    }

    public ItemStack getStackIn() {
        return this.stackIn;
    }

    public boolean isBlocked() {
        return this.isBlocked;
    }

    public void setBlocked(boolean blockEvent) {
        this.isBlocked = blockEvent;
    }
}
