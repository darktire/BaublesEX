package baubles.mixin.early.vanilla;

import baubles.util.ICapabilityModifiable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeEventFactory.class, remap = false)
public class MixinEventFactory {

    @Inject(method = "gatherCapabilities(Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/common/capabilities/ICapabilityProvider;)Lnet/minecraftforge/common/capabilities/CapabilityDispatcher;", at = @At("RETURN"))
    private static void redirectBaubleCap(ItemStack stack, ICapabilityProvider parent, CallbackInfoReturnable<CapabilityDispatcher> cir) {
        CapabilityDispatcher caps = cir.getReturnValue();
        if (caps != null) {
            ((ICapabilityModifiable) (Object) caps).patch(stack);
        }
    }
}
