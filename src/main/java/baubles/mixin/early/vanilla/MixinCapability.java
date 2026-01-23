package baubles.mixin.early.vanilla;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemData;
import baubles.util.ICapabilityModifiable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.*;

@Mixin(value = CapabilityDispatcher.class, remap = false)
@Implements(@Interface(iface = ICapabilityModifiable.class, prefix = "bs$"))
public abstract class MixinCapability {

    @Shadow
    private ICapabilityProvider[] caps;

    @Unique
    public void bs$patch(ItemStack stack) {
        if (BaublesCapabilities.CAPABILITY_ITEM_BAUBLE == null) return;
        for (int i = 0, j = this.caps.length; i < j; i++) {
            ICapabilityProvider provider = this.caps[i];
            if (provider.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
                IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                if (bauble != null && !(bauble instanceof BaublesWrapper)) {
                    if (!ItemData.isBauble(stack)) {
                        ItemData.registerBauble(stack, bauble.getBaubleType(stack).getExpansion());
                    }
                    this.caps[i] = new BaublesCapabilityProvider(stack, provider);
                    break;
                }
            }
        }
    }
}
