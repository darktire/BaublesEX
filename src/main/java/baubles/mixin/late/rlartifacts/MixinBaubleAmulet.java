package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleAmulet;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaubleAmulet.class)
public abstract class MixinBaubleAmulet implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if((Object) this == ModItems.PANIC_NECKLACE) return Resources.PANIC_MODEL;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return Resources.ULTIMATE_MODEL;
        else return Resources.AMULET_MODEL;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
