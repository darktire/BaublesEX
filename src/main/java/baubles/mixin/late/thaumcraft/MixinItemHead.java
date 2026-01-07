package baubles.mixin.late.thaumcraft;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.thaumcraft.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.armor.ItemGoggles;
import thaumcraft.common.items.baubles.ItemCuriosityBand;

@Mixin({ItemGoggles.class, ItemCuriosityBand.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemHead extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this == ItemsTC.goggles) return Resources.GOGGLES;
        else if (this == ItemsTC.bandCuriosity) return Resources.CURIOUS_BAND;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return IRenderBauble.RenderType.HEAD;
    }
}
