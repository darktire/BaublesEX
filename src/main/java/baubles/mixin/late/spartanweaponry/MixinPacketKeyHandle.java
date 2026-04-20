package baubles.mixin.late.spartanweaponry;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.oblivioussp.spartanweaponry.item.ItemQuiverBase;
import com.oblivioussp.spartanweaponry.network.PacketKeyHandle;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PacketKeyHandle.class, remap = false)
public class MixinPacketKeyHandle {
    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemQuiverBase.class);
    }
}
