package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBase;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelGlove;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBase.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleBase {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        if ((Object) this == ModItems.DRINKING_HAT) return ArtifactsResource.HAT_MODEL;
        else if ((Object) this == ModItems.POCKET_PISTON) return ModelGlove.instance((Item) (Object) this, slim);
        else if ((Object) this == ModItems.MAGMA_STONE) return ModelGlove.instance((Item) (Object) this, slim);
        return null;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        if ((Object) this == ModItems.POCKET_PISTON) return ModelGlove.instance((Item) (Object) this, slim).getTexture();
        else if ((Object) this == ModItems.MAGMA_STONE) return ModelGlove.instance((Item) (Object) this, slim).getLuminous();
        return null;
    }

    @Unique
    public ResourceLocation baubles$getLuminousTexture(boolean slim) {
        if ((Object) this == ModItems.MAGMA_STONE) return ModelGlove.instance((Item) (Object) this, slim).getLuminous();
        return null;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        if ((Object) this == ModItems.DRINKING_HAT) return IRenderBauble.RenderType.HEAD;
        else if ((Object) this == ModItems.POCKET_PISTON) return IRenderBauble.RenderType.BODY;
        else if ((Object) this == ModItems.MAGMA_STONE) return IRenderBauble.RenderType.BODY;
        return null;
    }
}
