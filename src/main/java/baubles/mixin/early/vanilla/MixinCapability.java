package baubles.mixin.early.vanilla;

import baubles.util.ICapabilityRemove;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(value = AttachCapabilitiesEvent.class, remap = false)
@Implements(@Interface(iface = ICapabilityRemove.class, prefix = "baubles$"))
public abstract class MixinCapability {
    @Shadow @Final private Map<ResourceLocation, ICapabilityProvider> caps;

    @Unique
    public void baubles$removeCap(ResourceLocation key) {
        this.caps.remove(key);
    }
}
