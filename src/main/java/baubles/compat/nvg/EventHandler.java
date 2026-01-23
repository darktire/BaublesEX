package baubles.compat.nvg;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.compat.ModOnly;
import com.noobanidus.nvg.compat.baubles.BaubleGoggles;
import com.noobanidus.nvg.init.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModOnly(value = "nvg")
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        ItemData.registerBauble(Items.goggles, new BaubleGoggles());
    }
}
