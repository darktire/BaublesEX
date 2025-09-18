package baubles.mixin.late.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.item.BaubleAmulet;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.compat.rlartifacts.Resources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BaubleAmulet.class)
@Implements(@Interface(iface = IRenderBauble.class, prefix = "brs$"))
public abstract class MixinBaubleAmulet {
    @Unique
    public ModelBauble brs$getModel(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if((Object) this == ModItems.PANIC_NECKLACE) return Resources.PANIC_MODEL;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return Resources.ULTIMATE_MODEL;
        else return Resources.AMULET_MODEL;
    }

    @Unique
    public ResourceLocation brs$getTexture(ItemStack stack, EntityLivingBase entity, boolean slim) {
        if ((Object) this == ModItems.SHOCK_PENDANT) return Resources.SHOCK_TEXTURE;
        else if((Object) this == ModItems.FLAME_PENDANT) return Resources.FLAME_TEXTURE;
        else if((Object) this == ModItems.THORN_PENDANT) return Resources.THORN_TEXTURE;
        else if((Object) this == ModItems.PANIC_NECKLACE) return Resources.PANIC_TEXTURE;
        else if((Object) this == ModItems.ULTIMATE_PENDANT) return Resources.ULTIMATE_TEXTURE;
        else if((Object) this == ModItems.SACRIFICIAL_AMULET) return Resources.SACRIFICIAL_TEXTURE;
        return null;
    }

    @Unique
    public IRenderBauble.RenderType brs$getRenderType(ItemStack stack, EntityLivingBase entity, boolean slim) {
        return IRenderBauble.RenderType.BODY;
    }
}
