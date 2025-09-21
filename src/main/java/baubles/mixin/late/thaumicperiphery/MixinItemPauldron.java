package baubles.mixin.late.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.thaumicperiphery.ModelPauldron;
import baubles.compat.thaumicperiphery.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumicperiphery.ModContent;
import thaumicperiphery.items.ItemPauldron;
import thaumicperiphery.items.ItemPauldronRepulsion;

import java.util.Map;

@Mixin({ItemPauldron.class, ItemPauldronRepulsion.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemPauldron extends Item {

    @Unique
    public Map<ModelBauble, IRenderBauble.RenderType> brs$getRenderMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return ModelPauldron.getRenderMap(slim);
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if (this == ModContent.pauldron) return Resources.PAULDRON;
        else if (this == ModContent.pauldron_repulsion) return Resources.PAULDRON_REPULSION;
        return null;
    }
}
