package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaublePotionEffect;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resource;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaublePotionEffect.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaublePotionEffect {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        if ((Object) this == ModItems.NIGHT_VISION_GOGGLES) return Resource.GOGGLES;
        else if ((Object) this == ModItems.SNORKEL) return Resource.SNORKEL;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.HEAD;
    }
}
