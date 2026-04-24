package baubles.mixin.late.thaumicaugmentation;

import baubles.api.BaubleType;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thecodex6824.thaumicaugmentation.common.event.PlayerEventHandler;

import java.util.stream.IntStream;

@Mixin(value = PlayerEventHandler.class, remap = false)
public class MixinPlayerEventHandler {
    @Redirect(method = {"playerCanBoost", "onFlyFall", "onFallHurt"}, at = @At(value = "INVOKE", target = "Lbaubles/api/BaubleType;getValidSlots()[I"))
    private static int[] redirect(BaubleType instance, @Local IBaublesItemHandler baubles) {
        return IntStream.range(0, baubles.getSlots()).toArray();
    }
}
