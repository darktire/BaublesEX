package baubles.mixin.late.ebwizardry;

import baubles.util.HookHelper;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemArtefact.class)
public abstract class MixinItem {
    @Inject(method = "initCapabilities", at = @At("RETURN"), cancellable = true, remap = false)
    private void setBaubleCap(ItemStack stack, NBTTagCompound nbt, CallbackInfoReturnable<ICapabilityProvider> cir) {
        cir.setReturnValue(HookHelper.redirectBaubleCap(stack, cir.getReturnValue()));
    }
}
