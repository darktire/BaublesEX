package baubles.mixin.late.thaumicaugmentation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "thecodex6824.thaumicaugmentation.common.item.ItemThaumostaticHarness$2", remap = false)
public class MixinItemThaumostaticHarness {
    @Mutable @Final @Shadow ItemStack val$stack;

    @Inject(method = "onWornTick", at = @At("HEAD"))
    private void inject(ItemStack itemstack, EntityLivingBase entity, CallbackInfo ci) {
        this.val$stack = itemstack;
    }
}
