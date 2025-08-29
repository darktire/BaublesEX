package baubles.mixin.late.artifacts;

import artifacts.common.item.BaubleObsidianSkull;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.Resource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleObsidianSkull.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "baubles$"))
public abstract class MixinBaubleObsidianSkull {
    @Unique
    public ModelBauble baubles$getModel(boolean slim) {
        return Resource.SKULL_MODEL;
    }

    @Unique
    public ResourceLocation baubles$getTexture(boolean slim, EntityLivingBase entity) {
        return Resource.OBSIDIAN_SKULL;
    }

    @Unique
    public IRenderBauble.RenderType baubles$getRenderType() {
        return IRenderBauble.RenderType.BODY;
    }
}
