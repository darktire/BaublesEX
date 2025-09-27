package baubles.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

@Event.HasResult
public class BaublesValidationEvent extends LivingEvent implements BaublesEvent.ResultAccessor {

    private final ItemStack stack;
    private final boolean ret;

    public BaublesValidationEvent(EntityLivingBase entity, ItemStack stack, boolean ret) {
        super(entity);
        this.stack = stack;
        this.ret = ret;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean getDefaultRet() {
        return this.ret;
    }
}
