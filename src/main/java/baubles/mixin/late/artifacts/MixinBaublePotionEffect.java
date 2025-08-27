package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaublePotionEffect;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaublePotionEffect.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaublePotionEffect {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        if ((Object) this == ModItems.NIGHT_VISION_GOGGLES) return ArtifactsResource.GOGGLES;
        else if ((Object) this == ModItems.SNORKEL) return ArtifactsResource.SNORKEL;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.HEAD;
    }
}
