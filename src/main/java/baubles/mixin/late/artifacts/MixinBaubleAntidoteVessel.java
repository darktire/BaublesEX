package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleAntidoteVessel;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleAntidoteVessel.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleAntidoteVessel {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        return Resources.ANTIDOTE_MODEL;
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        return Resources.ANTIDOTE_VESSEL;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
