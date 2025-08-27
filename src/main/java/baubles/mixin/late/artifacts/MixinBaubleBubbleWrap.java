package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleBubbleWrap;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBubbleWrap.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleBubbleWrap {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        return ArtifactsResource.BUBBLE_MODEL;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        return ArtifactsResource.BUBBLE_WRAP;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
