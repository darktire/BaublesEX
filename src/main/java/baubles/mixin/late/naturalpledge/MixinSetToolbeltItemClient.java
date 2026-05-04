package baubles.mixin.late.naturalpledge;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.wiresegal.naturalpledge.api.item.IPriestlyEmblem;
import com.wiresegal.naturalpledge.common.network.SetToolbeltItemClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SetToolbeltItemClient.class, remap = false)
public class MixinSetToolbeltItemClient {
    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    ItemStack inject(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, IPriestlyEmblem.class);
    }
}
