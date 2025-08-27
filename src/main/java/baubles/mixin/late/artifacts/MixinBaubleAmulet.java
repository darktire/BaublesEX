package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleAmulet;
import baubles.api.render.IRenderBauble;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleAmulet.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleAmulet {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        if((Object) this == ModItems.PANIC_NECKLACE) return ArtifactsResource.PANIC_MODEL;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return ArtifactsResource.ULTIMATE_MODEL;
        else return ArtifactsResource.AMULET_MODEL;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        if ((Object) this == ModItems.SHOCK_PENDANT) return ArtifactsResource.SHOCK_TEXTURE;
        else if((Object) this == ModItems.FLAME_PENDANT) return ArtifactsResource.FLAME_TEXTURE;
        else if((Object) this == ModItems.THORN_PENDANT) return ArtifactsResource.THORN_TEXTURE;
        else if((Object) this == ModItems.PANIC_NECKLACE) return ArtifactsResource.PANIC_TEXTURE;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return ArtifactsResource.ULTIMATE_TEXTURE;
        else if((Object) this == ModItems.SACRIFICIAL_AMULET) return ArtifactsResource.SACRIFICIAL_TEXTURE;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
