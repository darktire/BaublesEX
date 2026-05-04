package baubles.mixin.late.naturalpledge;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.wiresegal.naturalpledge.common.items.bauble.ItemDivineCloak;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemDivineCloak.Companion.class, remap = false)
public class MixinItemDivineCloak {
    @Redirect(method = "onFall", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    ItemStack inject0(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemDivineCloak.class);
    }

    @Redirect(method = "onDamage", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    ItemStack inject1(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemDivineCloak.class);
    }
}
