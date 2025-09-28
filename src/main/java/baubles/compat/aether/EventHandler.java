package baubles.compat.aether;

import baubles.api.BaubleTypeEx;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.event.EventHandlerItem;
import baubles.compat.ModOnly;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
            .put(AccessoryType.GLOVE, TypesData.Preset.RING)
            .put(AccessoryType.RING, TypesData.Preset.RING)
            .put(AccessoryType.PENDANT, TypesData.Preset.AMULET)
            .put(AccessoryType.CAPE, TypesData.Preset.BODY)
            .put(AccessoryType.MISC, TypesData.Preset.CHARM)
            .put(AccessoryType.SHIELD, TypesData.Preset.TRINKET)
            .build();

    static {
        for (AccessoryType a : AccessoryType.values()) {
            String name = a.toString().toLowerCase();
            map.put(a, TypesData.registerType(name, null, null, Collections.singleton(extension.get(a))));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;
        Item item = stack.getItem();
        if (ItemsData.isBauble(item)) return;
        if (AETHER_API.isAccessory(stack)) {
            if (!ItemsData.isBauble(item)) {
                ItemsData.registerBauble(item, map.get(AETHER_API.getAccessory(stack).getAccessoryType()));
                event.addCapability(EventHandlerItem.ITEM_CAP, new BaublesCapabilityProvider(stack, null));
            }
        }
    }
}
