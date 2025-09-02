package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaublePotionEffect;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaublePotionEffect.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaublePotionEffect {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        if ((Object) this == ModItems.NIGHT_VISION_GOGGLES) return Resources.GOGGLES;
        else if ((Object) this == ModItems.SNORKEL) return Resources.SNORKEL;
        return null;
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        if ((Object) this == ModItems.NIGHT_VISION_GOGGLES) return Resources.GOGGLES_TEXTURE;
        else if ((Object) this == ModItems.SNORKEL) return Resources.SNORKEL_TEXTURE;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.HEAD;
    }
}
