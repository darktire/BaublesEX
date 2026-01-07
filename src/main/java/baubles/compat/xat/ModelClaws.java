package baubles.compat.xat;

import baubles.api.model.ModelBauble;
import baubles.mixin.early.vanilla.AccessorRenderPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class ModelClaws extends ModelBauble {
    private final boolean isLeft;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/claws.png");

    public ModelClaws(boolean isLeft) {
        this.isLeft = isLeft;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (!TrinketsConfig.CLIENT.items.FAELIS_CLAW.doRender) {
            return;
        }

        final float offsetX = ((AccessorRenderPlayer) renderPlayer).isSlim() ? 12.4F : 18.6F;
        final float offsetY = 61F;
        final float offsetZ = -21F;
        final float bS = 0.16f;

        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        GlStateManager.translate((isLeft ? -1 : 1) * offsetX, offsetY, offsetZ);
        GlStateManager.rotate(-90F, 0F, 1F, 0F);
        DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);

    }

    public static ResourceLocation getTexture() {
        return TEXTURE;
    }
}
