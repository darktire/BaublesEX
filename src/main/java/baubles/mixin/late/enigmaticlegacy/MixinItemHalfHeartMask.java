package baubles.mixin.late.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.enigmaticlegacy.Resources;
import keletu.enigmaticlegacy.item.ItemHalfHeartMask;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemHalfHeartMask.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemHalfHeartMask {

    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return Resources.MASK_MODEL;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return Resources.OMINOUS_MASK;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.HEAD;
    }
}
