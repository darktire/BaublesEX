package baubles.mixin.late.rlartifacts;

import artifacts.common.item.BaubleAntidoteVessel;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaubleAntidoteVessel.class)
public abstract class MixinBaubleAntidoteVessel implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return Resources.ANTIDOTE_MODEL;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
