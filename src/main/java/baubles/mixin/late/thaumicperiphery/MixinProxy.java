package baubles.mixin.late.thaumicperiphery;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumicperiphery.proxy.ClientProxy;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinProxy {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lthaumicperiphery/proxy/ClientProxy;registerColorHandlers()V", shift = At.Shift.AFTER), cancellable = true)
    private void blockRendering(CallbackInfo ci) {
        ci.cancel();
    }
}
