package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBase;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.ModelGlove;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaubleBase.class)
public abstract class MixinBaubleBase extends Item implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.DRINKING_HAT) return Resources.HAT_MODEL;
        else return ModelGlove.getInstance(stack, renderPlayer);
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.DRINKING_HAT) return IRenderBauble.RenderType.HEAD;
        return null;
    }
}
