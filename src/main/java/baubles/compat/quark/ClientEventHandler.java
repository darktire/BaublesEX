package baubles.compat.quark;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.client.render.ArmorRender;
import baubles.compat.ModOnly;
import baubles.lib.util.ItemQuery;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.stream.Stream;

@ModOnly(value = "quark", client = true)
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        Item backpack = Item.getByNameOrId("quark:backpack");
        Item archaeologistHat = Item.getByNameOrId("quark:archaeologist_hat");
        Item witchHat = Item.getByNameOrId("quark:witch_hat");
        Item pirateHat = Item.getByNameOrId("quark:pirate_hat");
        Stream.of(backpack, archaeologistHat, witchHat, pirateHat)
                .filter(Objects::nonNull)
                .forEach(i -> ItemData.registerRender(ItemQuery.of(i), new ArmorRender(i, null)));
    }
}
