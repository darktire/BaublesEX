package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBottledCloud;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaubleBottledCloud.class)
public abstract class MixinBaubleBottledCloud implements IRenderBauble {
    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.BOTTLED_CLOUD) return Resources.BOTTLED_CLOUD;
        else if((Object) this == ModItems.BOTTLED_FART) return Resources.BOTTLED_FART;
        return null;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.BODY;
    }
}
