package baubles.compat.aether_legacy;

import baubles.api.BaubleTypeEx;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.common.event.EventHandlerItem;
import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.items.accessories.ItemAccessory;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class EventHandler {
    @SubscribeEvent
    public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;
        Item item = stack.getItem();
        if (item instanceof ItemAccessory) {
            if (!ItemsData.isBauble(item)) {
                ItemsData.registerBauble(item, map.get(((ItemAccessory) item).getType()));
                event.addCapability(EventHandlerItem.getItemCap(), new BaublesCapabilityProvider(stack, null));
            }
        }
    }

    private static final Map<AccessoryType, BaubleTypeEx> map = ImmutableMap.<AccessoryType, BaubleTypeEx>builder()
            .put(AccessoryType.GLOVE, TypesData.Preset.RING)
            .put(AccessoryType.RING, TypesData.Preset.RING)
            .put(AccessoryType.PENDANT, TypesData.Preset.AMULET)
            .put(AccessoryType.CAPE, TypesData.Preset.BODY)
            .put(AccessoryType.MISC, TypesData.Preset.CHARM)
            .put(AccessoryType.SHIELD, TypesData.Preset.TRINKET)
            .build();
}
