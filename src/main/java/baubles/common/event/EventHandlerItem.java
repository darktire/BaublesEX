package baubles.common.event;

import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
import static baubles.common.Baubles.MODID;

@SuppressWarnings("unused") // gets used by Forge event handler
public class EventHandlerItem {
    private static final ResourceLocation capabilityResourceLocation = new ResourceLocation(MODID, "bauble_cap");

    /**
     * Handles backwards compatibility with items that implement IBauble instead of exposing it as a capability.
     * This adds a IBauble capability wrapper for all items, if the item:
     * - does implement the IBauble interface
     * - does not already have the capability
     * - did not get the capability by another event handler earlier in the chain
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (stack.isEmpty()
                || !(stack.getItem() instanceof IBauble)
                || stack.hasCapability(CAPABILITY_ITEM_BAUBLE, null)
                || event.getCapabilities().values().stream().anyMatch(c -> c.hasCapability(CAPABILITY_ITEM_BAUBLE, null)))
            return;

        event.addCapability(capabilityResourceLocation, new BaublesCapabilityProvider(stack));
    }
}
