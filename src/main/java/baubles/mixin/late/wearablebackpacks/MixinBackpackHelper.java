package baubles.mixin.late.wearablebackpacks;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.util.CommonHelper;
import baubles.util.HookHelper;
import net.mcft.copy.backpacks.BackpacksContent;
import net.mcft.copy.backpacks.api.BackpackHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BackpackHelper.class, remap = false)
public class MixinBackpackHelper {
    @Inject(method = "getBackpackBaubleSlotItemStack", at = @At("HEAD"), cancellable = true)
    private static void get(EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
        cir.setReturnValue(HookHelper.getStack(baubles, BackpacksContent.BACKPACK));
    }

    @Inject(method = "setBackpackBaubleSlotItemStack", at = @At("HEAD"), cancellable = true)
    private static void set(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        if (stack.isEmpty()) {
            IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            int i = baubles.indexOf(BackpacksContent.BACKPACK, 0);
            if (i != -1) baubles.setStackInSlot(i, ItemStack.EMPTY);
        } else {
            CommonHelper.tryEquipping(player, stack);
        }
        ci.cancel();
    }
}
