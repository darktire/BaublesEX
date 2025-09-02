package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBottledCloud;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBottledCloud.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleBottledCloud {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        return Resources.BOTTLE_MODEL;
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        if ((Object) this == ModItems.BOTTLED_CLOUD) return Resources.BOTTLED_CLOUD;
        else if((Object) this == ModItems.BOTTLED_FART) return Resources.BOTTLED_FART;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
