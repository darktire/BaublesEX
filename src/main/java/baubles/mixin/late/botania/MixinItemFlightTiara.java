package baubles.mixin.late.botania;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

@Mixin(value = ItemFlightTiara.class, remap = false)
public class MixinItemFlightTiara {
    @Redirect(
            method = {"updatePlayerFlyStatus(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingUpdateEvent;)V", "shouldPlayerHaveFlight"},
            at = @At(
                    value = "INVOKE",
                    target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, this);
    }
}
