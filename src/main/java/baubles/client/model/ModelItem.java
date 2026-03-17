package baubles.client.model;

import baubles.api.model.ModelBauble;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelItem extends ModelBauble {

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IBakedModel model = getModel(stack);
        GlStateManager.pushMatrix();
        model = handleCameraTransforms(model);
        getItemRender().renderModel(model, -1, stack);
        GlStateManager.popMatrix();
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IBakedModel bakedModel = getModel(stack);
        if (bakedModel.isBuiltInRenderer()) return;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        bakedModel = handleCameraTransforms(bakedModel);
        getItemRender().renderEffect(bakedModel);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    protected IBakedModel getModel(ItemStack stack) {
        return getItemRender().getItemModelWithOverrides(stack, null, null);
    }

    public IBakedModel handleCameraTransforms(IBakedModel model) {
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        return model;
    }
}
