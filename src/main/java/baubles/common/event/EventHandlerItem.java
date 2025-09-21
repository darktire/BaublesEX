package baubles.common.event;

import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import net.minecraft.item.Item;
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
        Item item = stack.getItem();

        if (!(ItemsData.isBauble(item))) return;

        if (BaublesWrapper.CSTMap.isRemoved(item)) return;

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(stack, null));
    }
}
