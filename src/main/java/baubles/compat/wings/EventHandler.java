package baubles.compat.wings;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.compat.ModOnly;
import me.paulf.wings.server.item.ItemWings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ModOnly("wings")
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(ItemWings.class::isInstance)
                .forEach(i -> ItemsData.registerBauble(i, TypesData.Preset.BODY));
    }
}
