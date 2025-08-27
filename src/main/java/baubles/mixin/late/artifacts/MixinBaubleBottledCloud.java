package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBottledCloud;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBottledCloud.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleBottledCloud {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        return ArtifactsResource.BOTTLE_MODEL;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        if ((Object) this == ModItems.BOTTLED_CLOUD) return ArtifactsResource.BOTTLED_CLOUD;
        else if((Object) this == ModItems.BOTTLED_FART) return ArtifactsResource.BOTTLED_FART;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
