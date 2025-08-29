package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleStarCloak;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resource;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;


@Mixin(BaubleStarCloak.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleStarCloak {
    @Unique
    private static final ImmutableMap<ModelBauble, IRenderBauble.RenderType> brs$renderMap = ImmutableMap.of(
            Resource.CLOAK_MODEL_UP, IRenderBauble.RenderType.HEAD,
            Resource.CLOAK_MODEL_DOWN, IRenderBauble.RenderType.BODY
    );

    @Unique
    public Map<ModelBauble, IRenderBauble.RenderType> brs$getRenderMap(boolean slim) {
        return brs$renderMap;
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        return Resource.CLOAK_NORMAL;
    }

    @Unique
    public ResourceLocation brs$getEmissiveMap(boolean slim, EntityLivingBase entity) {
        return Resource.CLOAK_OVERLAY;
    }
}
