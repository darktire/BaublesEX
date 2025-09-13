package baubles.compat.xat;

import baubles.api.event.BaublesRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;

public class ClientEventHandler {

    @SubscribeEvent
    public static void equipmentRenderEvent(BaublesRenderEvent.InEquipments event) {
        if (!(event.getStack().getItem() instanceof TrinketEnderTiara)) {
            event.canceled();
        }
    }
}
