package baubles.mixin.late.xat;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.xat.ModelEnderTiara;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;

@Mixin(TrinketEnderTiara.class)
public class MixinTrinketEnderTiara implements IRenderBauble {

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelEnderTiara.instance;
    }

    @Override
    public IRenderBauble.RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.HEAD;
    }
}
