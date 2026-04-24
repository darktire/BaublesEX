package baubles.mixin.late.thaumicaugmentation;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "thecodex6824.thaumicaugmentation.common.item.ItemElytraHarness$2", remap = false)
public class MixinItemElytraHarness {
    @Inject(method = "onWornTick", at = @At(value = "FIELD", target = "Lthecodex6824/thaumicaugmentation/common/item/ItemElytraHarness$2;sync:Z"))
    private void inject(ItemStack stack, EntityLivingBase entity, CallbackInfo ci) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        int index = baubles.indexOf(stack, 0);
        baubles.stx.markDirty(index);
    }
}
