package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleObsidianSkull;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleObsidianSkull.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleObsidianSkull {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        return ArtifactsResource.SKULL_MODEL;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        return ArtifactsResource.OBSIDIAN_SKULL;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
