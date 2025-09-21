package baubles.compat.bot;

import baubles.api.event.BaublesRenderEvent;
import baubles.compat.ModOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModOnly(value = "botania", client = true)
public class ClientEventHandler {
    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent event) {
        if (shouldNotRender(event.getStack())) event.cancel();
    }

    private static boolean shouldNotRender(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("phantomInk");
    }
}
