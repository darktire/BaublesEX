package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.util.ItemsData;
import baubles.common.Baubles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused") // gets used by Forge event handler
public class EventHandlerItem {
    private static final ResourceLocation ITEM_CAP = new ResourceLocation(Baubles.MODID, "item_cap");
    /**
     * Attach bauble capability only for baubles do not already have the capability when creating stacks.
     * Some baubles get the capability by another event handler earlier in the chain may cause unexpected crash.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;
        Item item = stack.getItem();

        if (!(ItemsData.isBauble(item))) return;

        if (BaublesApi.isBauble(stack)) {
            ((BaublesCapabilityProvider)event.getCapabilities().get(ITEM_CAP)).serializeNBT();
            return;
        }

//        if (event.getCapabilities().values().stream().anyMatch(c -> c.hasCapability(CAPABILITY_ITEM_BAUBLE, null)))

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(stack));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemBaubleWrap(RegistryEvent.Register<Item> event) {
        Baubles.registry.registerItems();
    }
}
