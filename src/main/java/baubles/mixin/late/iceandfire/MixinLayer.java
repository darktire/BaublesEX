package baubles.mixin.late.iceandfire;

import com.github.alexthe666.iceandfire.integration.baubles.BaublesCompatBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BaublesCompatBridge.class, remap = false)
public abstract class MixinLayer {
    @Inject(method = "loadBaublesClientModels", at = @At("HEAD"), cancellable = true)
    private static void blockRendering(CallbackInfo ci) {
        ci.cancel();
    }
}
