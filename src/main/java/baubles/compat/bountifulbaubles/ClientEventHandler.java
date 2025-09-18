package baubles.compat.bountifulbaubles;

import baubles.api.event.BaublesRenderEvent;
import baubles.compat.ModOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.item.IPhantomInkable;

@ModOnly(value = "bountifulbaubles", client = true)
public class ClientEventHandler {
    @SubscribeEvent
    public static void applyControl(BaublesRenderEvent event) {
        if (doNotRender(event.getStack())) event.canceled();
    }


    private static boolean doNotRender(ItemStack stack) {
        return stack.getItem() instanceof IPhantomInkable &&((IPhantomInkable) stack.getItem()).hasPhantomInk(stack);
    }
}
