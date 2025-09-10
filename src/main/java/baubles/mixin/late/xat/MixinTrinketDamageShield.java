package baubles.mixin.late.xat;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.xat.ModelDamageShield;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;

@Mixin(TrinketDamageShield.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinTrinketDamageShield {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelDamageShield.instance();
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelDamageShield.getTexture();
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
