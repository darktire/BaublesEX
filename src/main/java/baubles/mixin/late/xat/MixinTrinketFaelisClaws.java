package baubles.mixin.late.xat;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.xat.ModelClaws;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xzeroair.trinkets.items.trinkets.TrinketFaelisClaws;

import java.util.Map;

@Mixin(TrinketFaelisClaws.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinTrinketFaelisClaws {

    @Unique
    public Map<ModelBauble, IRenderBauble.RenderType> brs$getRenderMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelClaws.getRenderMap(entity, slim);
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelClaws.getTexture();
    }
}
