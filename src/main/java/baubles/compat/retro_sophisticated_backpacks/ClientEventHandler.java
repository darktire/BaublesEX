package baubles.compat.retro_sophisticated_backpacks;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.client.render.ArmorRender;
import baubles.compat.ModOnly;
import baubles.lib.util.ItemQuery;
import com.cleanroommc.retrosophisticatedbackpacks.item.BackpackItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ModOnly(value = "retro_sophisticated_backpacks", client = true)
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(BackpackItem.class::isInstance)
                .forEach(i -> ItemData.registerRender(ItemQuery.of(i), new ArmorRender(i, String.valueOf(i.getRegistryName()))));
    }
}
