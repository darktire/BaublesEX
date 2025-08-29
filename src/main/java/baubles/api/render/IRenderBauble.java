package baubles.api.render;

import baubles.api.model.ModelBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * You can register render by implementing IRender or setting in {@link baubles.api.BaublesWrapper}.
 */
public interface IRenderBauble {

    default Map<ModelBauble, RenderType> getRenderMap(boolean slim) {
        return null;
    }

    default ModelBauble getModel(boolean slim) {
        return null;
    }

    default ResourceLocation getTexture(boolean slim, EntityLivingBase entity) {
        return null;
    }

    default ResourceLocation getEmissiveMap(boolean slim, EntityLivingBase entity) {
        return null;
    }

    default RenderType getRenderType() {
        return null;
    }

    @Deprecated
    default void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {}

    enum RenderType {
        HEAD,
        BODY,
        ARM_LEFT,
        ARM_RIGHT,
        LEG_LEFT,
        LEG_RIGHT,
        HEAD_WEAR,
        BODY_WEAR,
        ARM_LEFT_WEAR,
        ARM_RIGHT_WEAR,
        LEG_LEFT_WEAR,
        LEG_RIGHT_WEAR;
    }
}
