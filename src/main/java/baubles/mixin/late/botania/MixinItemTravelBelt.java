package baubles.mixin.late.botania;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

@Mixin(value = ItemTravelBelt.class, remap = false)
public class MixinItemTravelBelt {
    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "updatePlayerStepStatus")
    private ItemStack redirect1(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, this.getClass());
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "onPlayerJump")
    private ItemStack redirect2(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, this.getClass());
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "shouldPlayerHaveStepup")
    private ItemStack redirect3(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, this.getClass());
    }
}
