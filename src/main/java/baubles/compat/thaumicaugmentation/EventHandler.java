package baubles.compat.thaumicaugmentation;

import baubles.api.event.BaublesChangeEvent;
import baubles.compat.ModOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thecodex6824.thaumicaugmentation.common.internal.TAHooksCommon;

@ModOnly("thaumicaugmentation")
public class EventHandler {
    @SubscribeEvent
    public static void applyControl(BaublesChangeEvent event) {
        TAHooksCommon.onBaubleChanged(event.getEntityLiving());
    }
}
