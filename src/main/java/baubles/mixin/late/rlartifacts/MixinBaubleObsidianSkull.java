package baubles.mixin.late.rlartifacts;

import artifacts.common.item.BaubleObsidianSkull;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaubleObsidianSkull.class)
public abstract class MixinBaubleObsidianSkull implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return Resources.SKULL_MODEL;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
