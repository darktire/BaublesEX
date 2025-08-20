package baubles.common.event;

import baubles.Baubles;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.common.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static baubles.api.cap.BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;

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

        Map<ResourceLocation, ICapabilityProvider> capabilities = event.getCapabilities();
        Item item = stack.getItem();

        AtomicBoolean flag = new AtomicBoolean(false);
        capabilities.forEach((loc, provider) -> {
            IBauble bauble = provider.getCapability(CAPABILITY_ITEM_BAUBLE, null);
            if (bauble != null && !(bauble instanceof BaublesWrapper)) {
                capabilities.remove(loc);
                ItemsData.registerBauble(item, bauble);
            }
            else flag.set(true);
        });
        if (flag.get()) return;

        if (!(ItemsData.isBauble(item))) return;

        if (ItemsData.toBauble(item).getBauble() == null) return;

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(stack));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        Baubles.registry.registerItems();
        if (Config.rightClick) Baubles.config.setupBlacklist();
    }
}
