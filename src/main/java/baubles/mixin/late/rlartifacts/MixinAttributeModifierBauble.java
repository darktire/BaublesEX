package baubles.mixin.late.rlartifacts;

import artifacts.common.item.AttributeModifierBauble;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.ModelGlove;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AttributeModifierBauble.class)
public abstract class MixinAttributeModifierBauble extends Item implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelGlove.getInstance(stack, renderPlayer);
    }
}
