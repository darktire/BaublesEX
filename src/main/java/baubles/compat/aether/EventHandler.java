package baubles.compat.aether;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.compat.ModOnly;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ModOnly(value = "aether_legacy")
public class EventHandler {

    private static final AetherAPI AETHER_API = AetherAPI.getInstance();

    private static final Map<AccessoryType, BaubleTypeEx> map = new HashMap<>();
    private static final Map<AccessoryType, BaubleTypeEx> extension = ImmutableMap.<AccessoryType, BaubleTypeEx>builder()
            .put(AccessoryType.GLOVE, TypeData.Preset.RING)
            .put(AccessoryType.RING, TypeData.Preset.RING)
            .put(AccessoryType.PENDANT, TypeData.Preset.AMULET)
            .put(AccessoryType.CAPE, TypeData.Preset.BODY)
            .put(AccessoryType.MISC, TypeData.Preset.CHARM)
            .put(AccessoryType.SHIELD, TypeData.Preset.TRINKET)
            .build();

    private static void init() {}
    static {
        for (AccessoryType a : AccessoryType.values()) {
            String name = a.toString().toLowerCase();
            map.put(a, TypeData.registerType(name, null, null, Collections.singleton(extension.get(a))));
        }
    }

    @SubscribeEvent
    public static void onRegistering(RegistryEvent.Register<Item> event) {
        init(); // some mods like jei will trigger static initialization before, this is just for ensuring
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;
        if (ItemData.isBauble(stack)) return;
        if (AETHER_API.isAccessory(stack)) {
            if (!ItemData.isBauble(stack)) {
                ItemData.registerBauble(stack, map.get(AETHER_API.getAccessory(stack).getAccessoryType()));
//                if (!ItemData.isBauble(stack.getItem())) {
//                    event.addCapability(EventHandlerItem.ITEM_CAP, new BaublesCapabilityProvider(stack, null));
//                }
            }
        }
    }
}
