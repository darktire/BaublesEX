package baubles.mixin.late.randomthings;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import lumien.randomthings.item.ItemLavaCharm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemLavaCharm.class)
public class MixinItemLavaCharm {
    @Inject(method = "onWornTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setInteger(Ljava/lang/String;I)V"))
    private void inject(ItemStack stack, EntityLivingBase entity, CallbackInfo ci) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        int index = baubles.indexOf(stack, 0);
        baubles.getStx().markDirty(index);
    }
}
