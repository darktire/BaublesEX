package baubles.mixin.late.artifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.AttributeModifierBauble;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.artifacts.ModelGlove;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AttributeModifierBauble.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinAttributeModifierBauble extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelGlove.instance(this, slim);
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelGlove.instance(this, slim).getTexture();
    }

    @Unique
    public ResourceLocation brs$getEmissiveMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if((Object) this == ModItems.FIRE_GAUNTLET) return ModelGlove.instance(this, slim).getEmissive();
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
