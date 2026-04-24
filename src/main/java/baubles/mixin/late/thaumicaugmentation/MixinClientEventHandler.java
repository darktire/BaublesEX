package baubles.mixin.late.thaumicaugmentation;

import baubles.api.BaubleType;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thecodex6824.thaumicaugmentation.client.event.ClientEventHandler;

import java.util.stream.IntStream;

@Mixin(value = ClientEventHandler.class, remap = false)
public class MixinClientEventHandler {
    @Redirect(method = {"onClientTick", "onFlightChange"}, at = @At(value = "INVOKE", target = "Lbaubles/api/BaubleType;getValidSlots()[I"))
    private static int[] redirect(BaubleType instance, @Local IBaublesItemHandler baubles) {
        return IntStream.range(0, baubles.getSlots()).toArray();
    }
}
