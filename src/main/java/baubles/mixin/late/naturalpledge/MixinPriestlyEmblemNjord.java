package baubles.mixin.late.naturalpledge;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.HookHelper;
import com.wiresegal.naturalpledge.common.items.bauble.ItemIronBelt;
import com.wiresegal.naturalpledge.common.items.bauble.faith.PriestlyEmblemNjord;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PriestlyEmblemNjord.class, remap = false)
public class MixinPriestlyEmblemNjord {
    @Redirect(method = "floatInWater", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    ItemStack inject(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, ItemIronBelt.class);
    }
}
