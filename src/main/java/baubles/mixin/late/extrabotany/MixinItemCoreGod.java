package baubles.mixin.late.extrabotany;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.meteor.extrabotany.common.item.equipment.bauble.ItemCoreGod;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemCoreGod.class, remap = false)
public class MixinItemCoreGod {
    @Redirect(method = {"shouldPlayerHaveFlight", "updatePlayerFlyStatus"}, at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, this.getClass());
    }
}
