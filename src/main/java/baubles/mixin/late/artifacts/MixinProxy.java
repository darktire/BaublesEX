package baubles.mixin.late.artifacts;

import artifacts.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientProxy.class, remap = false)
public abstract class MixinProxy {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void blockRendering(CallbackInfo ci) {
        ci.cancel();
    }
}
