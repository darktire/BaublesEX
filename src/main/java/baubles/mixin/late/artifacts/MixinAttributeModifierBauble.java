package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.AttributeModifierBauble;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.ModelGlove;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AttributeModifierBauble.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinAttributeModifierBauble {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        return ModelGlove.instance((Item) (Object) this, slim);
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        return ModelGlove.instance((Item) (Object) this, slim).getTexture();
    }

    @Unique
    public ResourceLocation brs$getEmissiveMap(boolean slim, EntityLivingBase entity) {
        if((Object) this == ModItems.FIRE_GAUNTLET) return ModelGlove.instance((Item) (Object) this, slim).getEmissive();
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
