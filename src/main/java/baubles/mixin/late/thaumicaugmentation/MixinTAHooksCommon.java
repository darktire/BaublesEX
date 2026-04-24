package baubles.mixin.late.thaumicaugmentation;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thecodex6824.thaumicaugmentation.common.internal.TAHooksCommon;
import thecodex6824.thaumicaugmentation.common.item.trait.IElytraCompat;

@Mixin(value = TAHooksCommon.class, remap = false)
public class MixinTAHooksCommon {
    @Redirect(method = {"checkElytra", "updateElytraFlag"}, at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, IElytraCompat.class);
    }
}
