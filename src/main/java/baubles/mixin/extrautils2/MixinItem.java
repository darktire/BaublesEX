package baubles.mixin.extrautils2;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import com.rwtema.extrautils2.items.ItemAngelRing;
import com.rwtema.extrautils2.items.ItemChickenRing;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin({ItemAngelRing.class, ItemChickenRing.class})
public abstract class MixinItem {
    @Inject(method = "initCapabilities", at = @At("RETURN"), cancellable = true, remap = false)
    private void setBaubleCap(ItemStack stack, NBTTagCompound nbt, CallbackInfoReturnable<ICapabilityProvider> cir) {
        IBauble bauble = cir.getReturnValue().getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
        if (bauble instanceof BaublesWrapper) return;
        if (!ItemsData.isBauble(stack.getItem())) ItemsData.registerBauble(stack.getItem(), bauble.getBaubleType(null).getNewType());
        cir.setReturnValue(new BaublesCapabilityProvider(stack));
    }
}
