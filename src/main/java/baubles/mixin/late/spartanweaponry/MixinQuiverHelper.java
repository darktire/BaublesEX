package baubles.mixin.late.spartanweaponry;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.oblivioussp.spartanweaponry.item.ItemQuiverBase;
import com.oblivioussp.spartanweaponry.util.QuiverHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = QuiverHelper.class, remap = false)
public class MixinQuiverHelper {
    @Redirect(method = {"findFromBauble", "isInBaublesSlot"}, at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemQuiverBase.class);
    }
}
