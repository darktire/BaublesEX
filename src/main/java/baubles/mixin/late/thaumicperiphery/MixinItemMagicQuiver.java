package baubles.mixin.late.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.thaumicperiphery.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import thaumicperiphery.items.ItemMagicQuiver;

@Mixin(ItemMagicQuiver.class)
public class MixinItemMagicQuiver implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return Resources.MODEL_QUIVER;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
