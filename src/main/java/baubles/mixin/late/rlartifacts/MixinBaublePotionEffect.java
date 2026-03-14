package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaublePotionEffect;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaublePotionEffect.class)
public abstract class MixinBaublePotionEffect implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.NIGHT_VISION_GOGGLES) return Resources.GOGGLES;
        else if ((Object) this == ModItems.SNORKEL) return Resources.SNORKEL;
        return null;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.HEAD;
    }
}
