package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleStarCloak;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;


@Mixin(BaubleStarCloak.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleStarCloak {
    @Unique
    private static final ImmutableMap<ModelBase, IRenderBauble.RenderType> baubles$renderMap = ImmutableMap.of(
            ArtifactsResource.CLOAK_MODEL_UP, IRenderBauble.RenderType.HEAD,
            ArtifactsResource.CLOAK_MODEL_DOWN, IRenderBauble.RenderType.BODY
    );

    @Unique
    public Map<ModelBase, IRenderBauble.RenderType> baubles$getRenderMap(boolean slim) {
        return baubles$renderMap;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        return ArtifactsResource.CLOAK_NORMAL;
    }

    @Unique
    public ResourceLocation baubles$getLuminousTexture(boolean slim) {
        return ArtifactsResource.CLOAK_OVERLAY;
    }
}
