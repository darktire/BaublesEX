package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleBase;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.ModelGlove;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleBase.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleBase {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if ((Object) this == ModItems.DRINKING_HAT) return Resources.HAT_MODEL;
        else if ((Object) this == ModItems.POCKET_PISTON) return ModelGlove.instance((Item) (Object) this, slim);
        else if ((Object) this == ModItems.MAGMA_STONE) return ModelGlove.instance((Item) (Object) this, slim);
        return null;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if ((Object) this == ModItems.DRINKING_HAT) {
            if (entity instanceof EntityPlayer && entity.getName().equals("wouterke")) {
                return Resources.HAT_SPECIAL_TEXTURE;
            }
            else return Resources.HAT_TEXTURE;
        }
        else if ((Object) this == ModItems.POCKET_PISTON) return ModelGlove.instance((Item) (Object) this, slim).getTexture();
        return null;
    }

    @Unique
    public ResourceLocation brs$getEmissiveMap(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if ((Object) this == ModItems.MAGMA_STONE) return ModelGlove.instance((Item) (Object) this, slim).getEmissive();
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if ((Object) this == ModItems.DRINKING_HAT) return IRenderBauble.RenderType.HEAD;
        else if ((Object) this == ModItems.POCKET_PISTON) return IRenderBauble.RenderType.BODY;
        else if ((Object) this == ModItems.MAGMA_STONE) return IRenderBauble.RenderType.BODY;
        return null;
    }
}
