package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.AttributeModifierBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelGlove;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AttributeModifierBauble.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinAttributeModifierBauble {
    @Unique
    public ModelBase baubles$getModel(boolean slim) {
        return ModelGlove.instance((Item) (Object) this, slim);
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim) {
        return ModelGlove.instance((Item) (Object) this, slim).getTexture();
    }

    @Unique
    public ResourceLocation baubles$getLuminousTexture(boolean slim) {
        if((Object) this == ModItems.FIRE_GAUNTLET) return ModelGlove.instance((Item) (Object) this, slim).getLuminous();
        return null;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
