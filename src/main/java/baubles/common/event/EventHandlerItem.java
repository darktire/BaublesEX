package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
public class EventHandlerItem {
    public static final ResourceLocation ITEM_CAP = new ResourceLocation(BaublesApi.MOD_ID, "item_cap");
    /**
     * Attach bauble capability only for baubles do not already have the capability when creating stacks.
     * Some baubles get the capability by another event handler earlier will be removed and attach the capability provided by BaublesEX.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;

        if (!(ItemData.isBauble(stack.getItem()))) return;

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(stack, null));
    }
}
