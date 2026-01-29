package baubles.mixin.late.backpacked;

import baubles.api.cap.IBaublesItemHandler;
import baubles.util.CommonHelper;
import baubles.util.HookHelper;
import com.mrcrayfish.backpacked.integration.Baubles;
import com.mrcrayfish.backpacked.item.BaubleBackpackItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Baubles.class, remap = false)
public class MixinIntegration {
    @Inject(method = {"playerDeathHigh", "playerDeathLow"}, at = @At("HEAD"), cancellable = true)
    private void inject(PlayerDropsEvent event, CallbackInfo ci) {
        ci.cancel();
    }


    @Redirect(method = "getBackpackStack", at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack redirect(IBaublesItemHandler instance, int i) {
        return HookHelper.getStack(instance, BaubleBackpackItem.class);
    }

    @Inject(method = "setBackpackStack", at = @At("HEAD"), cancellable = true)
    private static void inject(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        CommonHelper.tryEquipping(player, stack);
        ci.cancel();
    }
}
