package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.common.Config;
import baubles.common.extra.BaubleItemContent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static baubles.common.Baubles.MODID;

@SuppressWarnings("unused") // gets used by Forge event handler
public class EventHandlerItem {
    private static final ResourceLocation BaubleItemCap = new ResourceLocation(MODID, "bauble");

    /**
     * Attach bauble capability only for baubles do not already have the capability when creating stacks.
     * Some baubles get the capability by another event handler earlier in the chain may cause unexpected crash.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;
        Item item = stack.getItem();

        if (!(item instanceof IBauble)) return;

        if (BaublesApi.isBauble(stack)) {
            ((BaublesCapabilityProvider)event.getCapabilities().get(BaubleItemCap)).serializeNBT();
        }

//        if (event.getCapabilities().values().stream().anyMatch(c -> c.hasCapability(CAPABILITY_ITEM_BAUBLE, null)))

        if (Config.jsonFunction && !BaubleItemContent.isInit) {
            BaubleItemContent content = new BaubleItemContent();
            content.registerItem(item);
        }

        event.addCapability(BaubleItemCap, new BaublesCapabilityProvider(stack));
    }
}
