package baubles.mixin.late.nvg;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.noobanidus.nvg.compat.baubles.goggles.BaubleInventoryUtil;
import com.noobanidus.nvg.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BaubleInventoryUtil.class, remap = false)
public class MixinBaubleInventoryUtil {

    @Redirect(method = "getGoggles", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, Items.goggles);
    }
}
