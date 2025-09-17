package baubles.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BaublesEvent extends LivingEvent {
    private final ItemStack stack;

    public BaublesEvent(EntityLivingBase entity, ItemStack stack) {
        super(entity);
        this.stack = stack;
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

        @HasResult
        public static class Pre extends Equip implements ResultAccessor {

            private final boolean ret;

            public Pre(EntityLivingBase entity, ItemStack stack, boolean ret) {
                super(entity, stack);
                this.ret = ret;
            }

            @Override
            public boolean getDefaultRet() {
                if (this.isCanceled()) return false;
                return this.ret;
            }
        }

        public static class Post extends Equip {

            public Post(EntityLivingBase entity, ItemStack stack) {
                super(entity, stack);
            }
        }
    }

    public static class Unequip extends BaublesEvent {

        public Unequip(EntityLivingBase entity, ItemStack stack) {
            super(entity, stack);
        }

        @HasResult
        public static class Pre extends Unequip implements ResultAccessor {

            private final boolean ret;

            public Pre(EntityLivingBase entity, ItemStack stack, boolean ret) {
                super(entity, stack);
                this.ret = ret;
            }

            @Override
            public boolean getDefaultRet() {
                if (this.isCanceled()) return false;
                return this.ret;
            }
        }

        public static class Post extends Unequip {

            public Post(EntityLivingBase entity, ItemStack stack) {
                super(entity, stack);
            }
        }
    }

    public interface ResultAccessor {

        Event.Result getResult();
        boolean getDefaultRet();

        default boolean getRet() {
            switch (getResult()) {
                case DENY:  return false;
                case ALLOW: return true;
                default:    return getDefaultRet();
            }
        }
    }
}
