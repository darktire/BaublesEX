package baubles.mixin.late.spartanweaponry;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.oblivioussp.spartanweaponry.client.gui.GuiHandler;
import com.oblivioussp.spartanweaponry.item.ItemQuiverBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiHandler.class, remap = false)
public class MixinGuiHandler {
    @Redirect(method = "findQuiverStack", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemQuiverBase.class);
    }
}
