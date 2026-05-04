package baubles.mixin.late.naturalpledge;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.wiresegal.naturalpledge.common.block.trap.BlockBaseTrap;
import com.wiresegal.naturalpledge.common.items.bauble.ItemDivineCloak;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockBaseTrap.class, remap = false)
public class MixinBlockBaseTrap {
    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    ItemStack inject(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemDivineCloak.class);
    }
}
