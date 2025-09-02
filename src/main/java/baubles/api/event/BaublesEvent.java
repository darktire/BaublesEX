package baubles.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public class BaublesEvent extends LivingEvent {
    private final ItemStack stack;

    public BaublesEvent(EntityLivingBase entity, ItemStack stack) {
        super(entity);
        this.stack = stack;
    }

    public boolean isCancelable()
    {
        return true;
    }

    public void canceled() {
        this.setCanceled(true);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public static class WearingTick extends BaublesEvent {

        public WearingTick(EntityLivingBase entity, ItemStack stack) {
            super(entity, stack);
        }
    }

    public static class Equip extends BaublesEvent {

        public Equip(EntityLivingBase entity, ItemStack stack) {
            super(entity, stack);
        }
    }

    public static class Unequip extends BaublesEvent {

        public Unequip(EntityLivingBase entity, ItemStack stack) {
            super(entity, stack);
        }
    }
}
