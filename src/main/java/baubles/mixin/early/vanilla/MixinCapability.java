package baubles.mixin.early.vanilla;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemsData;
import baubles.util.ICapabilityModifiable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.*;

@Mixin(value = CapabilityDispatcher.class, remap = false)
@Implements(@Interface(iface = ICapabilityModifiable.class, prefix = "baubles$"))
public abstract class MixinCapability {

    @Shadow private ICapabilityProvider[] caps;

    @Unique
    public void baubles$patchCap(ItemStack stack) {
        for (int i = 0, j = this.caps.length; i < j; i++) {
            ICapabilityProvider provider = this.caps[i];
            IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
            if (bauble != null && !(bauble instanceof BaublesWrapper)) {
                if (!ItemsData.isBauble(stack.getItem())) {
                    ItemsData.registerBauble(stack.getItem(), bauble.getBaubleType(stack).getExpansion());
                }
                provider = new BaublesCapabilityProvider(stack, provider);
                this.caps[i] = provider;
                break;
            }
        }
    }
}
