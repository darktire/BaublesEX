package baubles.mixin.late.botania;

import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemWaterRing;

@Mixin(value = ItemWaterRing.class, remap = false)
public class MixinItemWaterRing {
    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "onWornTick")
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        return instance.getStackInSlot(instance.indexOf(ModItems.waterRing, 0));
    }
}
