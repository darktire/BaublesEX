package baubles.compat.rlartifacts;

import artifacts.common.init.ModItems;
import artifacts.common.util.RenderHelper;
import baubles.api.BaublesApi;
import baubles.client.model.ModelInherit;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelCloak extends ModelInherit {
    public static final ResourceLocation CLOAK_NORMAL = Resources.getLoc("star_cloak.png");
    public static final ResourceLocation CLOAK_OVERLAY = Resources.getLoc("star_cloak_overlay.png");

    public ModelCloak(boolean isUp) {
        super(isUp ? new Up() : new Down(), null);
    }

    @Override
    public ResourceLocation getEmissiveMap(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return CLOAK_NORMAL;
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return CLOAK_OVERLAY;
    }

    private static class Up extends artifacts.client.model.ModelCloak {
        @Override
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {

            this.renderHood(entity, 0, 0, 0, 0, 0, scale, ModelCloak.renderHood(entity));
        }
    }

    private static class Down extends artifacts.client.model.ModelCloak {
        @Override
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {
            this.renderCloak(entity, 0, 0, 0, 0, 0, scale, ModelCloak.renderHood(entity));
        }
    }

    private static boolean renderHood(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return RenderHelper.shouldRenderInSlot((EntityPlayer) entity, EntityEquipmentSlot.HEAD) && (BaublesApi.getIndexInBaubles((EntityLivingBase) entity, ModItems.DRINKING_HAT, 0) == -1);
        }
        return false;
    }
}
