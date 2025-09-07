package baubles.mixin.late.bountifulbaubles;

import cursedflames.bountifulbaubles.proxy.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientProxy.class)
public abstract class MixinProxy {
    @Inject(method = "addRenderLayer", at = @At("HEAD"), cancellable = true, remap = false)
    private void blockRender(CallbackInfo ci) {
        ci.cancel();
    }
}
