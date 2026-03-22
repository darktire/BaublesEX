package baubles.compat.tombstone;

import baubles.common.event.BaubleDropsEvent;
import baubles.compat.ModOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ovh.corail.tombstone.helper.EntityHelper;

@ModOnly("tombstone")
public class EventHandler {

    @SubscribeEvent
    public static void onBaublesDrop(BaubleDropsEvent event) {
        ItemStack stack = event.getStack();
        if (EntityHelper.hasSoulbound(stack)) {
            event.noDrops();
        }
    }
}
