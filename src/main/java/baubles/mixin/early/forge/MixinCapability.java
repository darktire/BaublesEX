package baubles.mixin.early.forge;

import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilityProvider;
import baubles.api.registries.ItemData;
import baubles.lib.util.ItemQuery;
import baubles.util.ICapabilityModifiable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CapabilityDispatcher.class, remap = false)
public abstract class MixinCapability implements ICapabilityModifiable {

    @Shadow
    private ICapabilityProvider[] caps;

    @Override
    public void patch(ItemStack stack) {
        if (BaublesCapabilities.CAPABILITY_ITEM_BAUBLE == null) return;
        for (int i = 0, j = this.caps.length; i < j; i++) {
            ICapabilityProvider provider = this.caps[i];
            if (provider.hasCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null)) {
                IBauble bauble = provider.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE, null);
                if (bauble != null && !(bauble instanceof BaublesWrapper)) {
                    ItemQuery query = ItemQuery.of(stack);
                    if (!ItemData.isBauble(query)) {
                        ItemData.registerBauble(query, bauble.getBaubleType(stack).getExpansion());
                    }
                    this.caps[i] = new BaublesCapabilityProvider(stack, provider);
                    break;
                }
            }
        }
    }
}
