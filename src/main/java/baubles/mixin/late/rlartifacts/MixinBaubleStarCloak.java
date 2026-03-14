package baubles.mixin.late.rlartifacts;

import artifacts.common.item.BaubleStarCloak;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.CloakRender;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;


@Mixin(BaubleStarCloak.class)
public abstract class MixinBaubleStarCloak implements IRenderBauble {

    @Override
    public List<IRenderBauble> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return CloakRender.SUB;
    }
}
