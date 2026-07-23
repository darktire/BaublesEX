package baubles.compat.quark;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.client.render.ArmorRender;
import baubles.compat.ModOnly;
import baubles.lib.util.ItemQuery;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.oddities.feature.Backpacks;
import vazkii.quark.vanity.feature.WitchHat;
import vazkii.quark.world.feature.Archaeologist;
import vazkii.quark.world.feature.PirateShips;

import java.util.Objects;
import java.util.stream.Stream;

@ModOnly(value = "quark", client = true)
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        Stream.of(Backpacks.backpack, Archaeologist.archaeologist_hat, WitchHat.witch_hat, PirateShips.pirate_hat)
                .filter(Objects::nonNull)
                .forEach(i -> ItemData.registerRender(ItemQuery.of(i), new ArmorRender(i, null)));
    }
}
