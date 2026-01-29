package baubles.mixin.late.colytra;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import c4.colytra.util.ColytraUtil;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ColytraUtil.class, remap = false)
public class MixinColytraUtil {

    @Redirect(method = "wornElytraBauble", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemElytra.class);
    }
}
