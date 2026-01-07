package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBase;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.ModelGlove;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBase.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleBase extends Item {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.DRINKING_HAT) return Resources.HAT_MODEL;
        else return ModelGlove.getInstance(stack, renderPlayer);
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if ((Object) this == ModItems.DRINKING_HAT) return IRenderBauble.RenderType.HEAD;
        else if ((Object) this == ModItems.POCKET_PISTON) return IRenderBauble.RenderType.BODY;
        else if ((Object) this == ModItems.MAGMA_STONE) return IRenderBauble.RenderType.BODY;
        return null;
    }
}
