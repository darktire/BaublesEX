package baubles.mixin.early.vanilla;

import baubles.api.AbstractWrapper;
import baubles.api.BaublesApi;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Inject(method = "canApply", at = @At("RETURN"), cancellable = true)
    private void canEnchantBaubles0(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean flag = cir.getReturnValue();
        if (flag) return;

        AbstractWrapper bauble = BaublesApi.toBauble(stack);
        if (bauble == null) return;
        flag = bauble.canApply(stack, (Enchantment) (Object) this);
        cir.setReturnValue(flag);
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("RETURN"), cancellable = true, remap = false)
    private void canEnchantBaubles1(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean flag = cir.getReturnValue();
        if (flag) return;

        AbstractWrapper bauble = BaublesApi.toBauble(stack);
        if (bauble == null) return;
        flag = bauble.canApply(stack, (Enchantment) (Object) this);
        cir.setReturnValue(flag);
    }
}
