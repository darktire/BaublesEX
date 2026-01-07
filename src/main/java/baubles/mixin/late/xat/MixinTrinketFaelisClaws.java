package baubles.mixin.late.xat;

import baubles.api.render.IRenderBauble;
import baubles.compat.xat.ClawsRender;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xzeroair.trinkets.items.trinkets.TrinketFaelisClaws;

import java.util.List;

@Mixin(TrinketFaelisClaws.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinTrinketFaelisClaws {

    @Unique
    public List<IRenderBauble> brs$getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ClawsRender.get(entity);
    }
}
