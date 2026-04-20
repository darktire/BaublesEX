package baubles.mixin.late.totemexpansion;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.totemexpansion.handler.TotemEventHandler;
import party.lemons.totemexpansion.item.ItemTotemBase;
import party.lemons.totemexpansion.misc.TotemUtil;

@Mixin(value = TotemEventHandler.class, remap = false)
public class MixinTotemEventHandler {
    @Inject(method = "activateTotem", at = @At("HEAD"), cancellable = true)
    private static void redirect(EntityPlayer living, ItemStack stack, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (TotemUtil.isTotemBlacklisted(stack.getItem())) {
            cir.setReturnValue(false);
        } else {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) living);
            int index = baubles.indexOf(stack, 0);
            boolean result = ((ItemTotemBase)stack.getItem()).onActivate(living, stack, source);
            if (index != -1) baubles.stx.markDirty(index);
            cir.setReturnValue(result);
        }
    }
}
