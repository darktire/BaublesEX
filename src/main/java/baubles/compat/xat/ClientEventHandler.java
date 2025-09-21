package baubles.compat.xat;

import baubles.api.event.BaublesRenderEvent;
import baubles.compat.ModOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;

@ModOnly(value = "xat", client = true)
public class ClientEventHandler {

    @SubscribeEvent
    public static void equipmentRenderEvent(BaublesRenderEvent.InEquipments event) {
        if (event.getStack().getItem() instanceof TrinketEnderTiara) {
            event.cancel();
        }
    }
}
