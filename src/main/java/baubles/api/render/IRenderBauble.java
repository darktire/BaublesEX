package baubles.api.render;

import baubles.api.model.ModelBauble;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * You can register render by implementing IRender or setting in {@link baubles.api.BaublesWrapper}.
 */
public interface IRenderBauble {

    default <T extends IRenderBauble> List<T> getSubRender(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return null;
    }

    default ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return null;
    }

    default RenderType getRenderType(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return getRenderType();
    }

    @Deprecated
    default RenderType getRenderType() {
        return null;
    }

    @Deprecated
    default void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {}

    @Deprecated
    final class Helper {

        public static void rotateIfSneaking(EntityPlayer player) {}

        public static void applySneakingRotation() {}

        public static void translateToHeadLevel(EntityPlayer player) {}

        public static void translateToFace() {}

        public static void defaultTransforms() {}

        public static void translateToChest() {}
    }

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
        LEG_RIGHT_WEAR
    }
}
