package baubles.mixin.late.thaumicperiphery;

import baubles.api.render.IRenderBauble;
import baubles.compat.thaumicperiphery.PauldronRender;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumicperiphery.ModContent;
import thaumicperiphery.items.ItemPauldron;
import thaumicperiphery.items.ItemPauldronRepulsion;

import java.util.List;

@Mixin({ItemPauldron.class, ItemPauldronRepulsion.class})
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public class MixinItemPauldron extends Item {

    @Unique
    public List<IRenderBauble> brs$getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (this == ModContent.pauldron) return PauldronRender.PAULDRON;
        else if (this == ModContent.pauldron_repulsion) return PauldronRender.PAULDRON_REPULSION;
        return null;
    }

}
