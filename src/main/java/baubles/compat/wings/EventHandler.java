package baubles.compat.wings;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.compat.ModOnly;
import me.paulf.wings.server.item.ItemWings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ModOnly("wings")
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(ItemWings.class::isInstance)
                .forEach(i -> ItemData.registerBauble(i, TypeData.Preset.BODY));
    }
}
