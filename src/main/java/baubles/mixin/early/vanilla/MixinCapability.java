package baubles.mixin.early.vanilla;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.util.ICapabilityModifiable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CapabilityDispatcher.class, remap = false)
@Implements(@Interface(iface = ICapabilityModifiable.class, prefix = "bs$"))
public abstract class MixinCapability {

    @Shadow
    private ICapabilityProvider[] caps;

    @Unique
    private ItemStack bs$stack;
    @Unique
    private boolean bs$tryRedirect = false;
    @Unique
    public void bs$patchCap(ItemStack stack) {
        this.bs$stack = stack;
        this.bs$tryRedirect = true;
    }

    @Inject(method = "getCapability", at = @At("HEAD"))
    private <T> void injected(Capability<T> capability, EnumFacing facing, CallbackInfoReturnable<T> cir) {
        if (this.bs$tryRedirect && capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE) {
            for (int i = 0, j = this.caps.length; i < j; i++) {
                ICapabilityProvider provider = this.caps[i];
                IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                if (bauble != null && !(bauble instanceof BaublesWrapper)) {
                    if (!ItemsData.isBauble(this.bs$stack.getItem())) {
                        ItemsData.registerBauble(this.bs$stack.getItem(), bauble.getBaubleType(this.bs$stack).getExpansion());
                    }
                    this.caps[i] = new BaublesCapabilityProvider(this.bs$stack, provider);
                    break;
                }
            }
            this.bs$tryRedirect = false;
        }
    }
}
