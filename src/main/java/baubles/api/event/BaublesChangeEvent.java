package baubles.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BaublesChangeEvent extends LivingEvent {
    private final EntityLivingBase target;
    private final int index;
    private final ItemStack stackOut;
    private final ItemStack stackIn;

    public BaublesChangeEvent(EntityLivingBase target, int index, ItemStack out, ItemStack in) {
        super(target);
        this.target = target;
        this.index = index;
        this.stackOut = out;
        this.stackIn = in;
    }

    public EntityLivingBase getTarget() {
        return this.target;
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
}
