package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleAmulet;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleAmulet.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleAmulet {
    @Unique
    public ModelBauble brs$getModel(boolean slim) {
        if((Object) this == ModItems.PANIC_NECKLACE) return Resource.PANIC_MODEL;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return Resource.ULTIMATE_MODEL;
        else return Resource.AMULET_MODEL;
    }

    @Unique
    public ResourceLocation brs$getTexture(boolean slim, EntityLivingBase entity) {
        if ((Object) this == ModItems.SHOCK_PENDANT) return Resource.SHOCK_TEXTURE;
        else if((Object) this == ModItems.FLAME_PENDANT) return Resource.FLAME_TEXTURE;
        else if((Object) this == ModItems.THORN_PENDANT) return Resource.THORN_TEXTURE;
        else if((Object) this == ModItems.PANIC_NECKLACE) return Resource.PANIC_TEXTURE;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return Resource.ULTIMATE_TEXTURE;
        else if((Object) this == ModItems.SACRIFICIAL_AMULET) return Resource.SACRIFICIAL_TEXTURE;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
