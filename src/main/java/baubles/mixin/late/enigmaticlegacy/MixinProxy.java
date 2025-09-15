package baubles.mixin.late.enigmaticlegacy;

import keletu.enigmaticlegacy.proxy.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientProxy.class)
public abstract class MixinProxy {
    @Inject(method = "addRenderLayers", at = @At("HEAD"), cancellable = true, remap = false)
    private static void blockRendering(CallbackInfo ci) {
        ci.cancel();
    }
}
