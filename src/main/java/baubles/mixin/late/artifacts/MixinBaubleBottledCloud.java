package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleBottledCloud;
import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BaubleBottledCloud.class, remap = false)
public class MixinBaubleBottledCloud {
    @Redirect(method = "onClientTick", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, BaubleBottledCloud.class);
    }
}
