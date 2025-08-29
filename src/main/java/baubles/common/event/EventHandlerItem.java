package baubles.common.event;

import baubles.Baubles;
import baubles.BaublesRegister;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.common.config.Config;
import baubles.util.ICapabilityRemove;
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
    private static final ResourceLocation ITEM_CAP = new ResourceLocation(Baubles.MOD_ID, "item_cap");
    /**
     * Attach bauble capability only for baubles do not already have the capability when creating stacks.
     * Some baubles get the capability by another event handler earlier will be removed and attach the capability provided by BaublesEX.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();

        if (stack.isEmpty()) return;

        Map<ResourceLocation, ICapabilityProvider> capabilities = event.getCapabilities();
        Item item = stack.getItem();

        AtomicBoolean hadCap = new AtomicBoolean(false);
        capabilities.keySet().stream().filter(loc -> {
            IBauble bauble = capabilities.get(loc).getCapability(CAPABILITY_ITEM_BAUBLE, null);
            if (bauble instanceof BaublesWrapper) hadCap.set(true);
            else if (bauble != null) {
                ItemsData.registerBauble(item, bauble);
                return true;
            }
            return false;
        }).findAny().ifPresent(loc -> ((ICapabilityRemove) event).removeCap(loc));
        if (hadCap.get()) return;

        if (!(ItemsData.isBauble(item))) return;

        if (ItemsData.toBauble(item).getBauble() == null) return;

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(item));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        BaublesRegister.registerItems();
        if (Config.rightClick) Config.setupBlacklist();
    }
}
