package baubles.mixin.late.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.thaumicperiphery.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumicperiphery.items.ItemMagicQuiver;

@Mixin(ItemMagicQuiver.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemMagicQuiver {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return Resources.MODEL_QUIVER;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return Resources.QUIVER_TEXTURE;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
