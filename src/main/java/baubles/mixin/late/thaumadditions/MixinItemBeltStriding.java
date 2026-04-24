package baubles.mixin.late.thaumadditions;

import baubles.api.BaubleType;
import baubles.api.cap.IBaublesItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.zeith.thaumicadditions.items.baubles.ItemBeltStriding;

import java.util.stream.IntStream;

@Mixin(value = ItemBeltStriding.class, remap = false)
public class MixinItemBeltStriding {
    @Redirect(method = {"playerJumps", "playerFalls"}, at = @At(value = "INVOKE", target = "Lbaubles/api/BaubleType;getValidSlots()[I"))
    private int[] redirect(BaubleType instance, @Local IBaublesItemHandler baubles) {
        return IntStream.range(0, baubles.getSlots()).toArray();
    }
}
