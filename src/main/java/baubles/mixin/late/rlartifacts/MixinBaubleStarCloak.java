package baubles.mixin.late.rlartifacts;

import artifacts.common.item.BaubleStarCloak;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.CloakRender;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;


@Mixin(BaubleStarCloak.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleStarCloak {

    @Unique
    public List<IRenderBauble> brs$getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return CloakRender.SUB;
    }
}
