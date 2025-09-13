package baubles.common.event;

import baubles.Baubles;
import baubles.BaublesRegister;
import baubles.api.BaubleTypeEx;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.common.config.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Baubles.MOD_ID)
public class EventHandlerItem {
    private static final ResourceLocation ITEM_CAP = new ResourceLocation(Baubles.MOD_ID, "item_cap");
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

        if (ItemsData.toBauble(item).getBauble() == null) return;

        event.addCapability(ITEM_CAP, new BaublesCapabilityProvider(item));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void itemBaubleWrap(RegistryEvent.Register<BaubleTypeEx> event) {
        BaublesRegister.registerItems();
        if (Config.rightClick) Config.setupBlacklist();
    }

    public static ResourceLocation getItemCap() {
        return ITEM_CAP;
    }
}
