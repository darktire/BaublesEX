package baubles.client.model;

import baubles.api.model.ModelBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

public class ModelBaked extends ModelBauble {

    private final ModelResourceLocation mrl;

    public ModelBaked(ModelResourceLocation loc) {
        this.mrl = loc;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        IBakedModel model = Models.getBakedModel(this.mrl);
        GlStateManager.pushMatrix();
        model = handleCameraTransforms(model);
        getItemRender().renderModel(model, -1, ItemStack.EMPTY);
        GlStateManager.popMatrix();
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IBakedModel bakedModel = Models.getBakedModel(this.mrl);
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

    protected static RenderItem getItemRender() {
        return Minecraft.getMinecraft().getRenderItem();
    }

    public IBakedModel handleCameraTransforms(IBakedModel model) {
        Pair<? extends IBakedModel, Matrix4f> pair = model.handlePerspective(ItemCameraTransforms.TransformType.HEAD);

        GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.625F, 0.625F, 0.625F);
        GlStateManager.translate(-0.5F, -0.125F , -0.5F);

        if (pair.getRight() != null) {
            GlStateManager.translate(0.5F, 0.5F , 0.5F);
            ForgeHooksClient.multiplyCurrentGlMatrix(pair.getRight());
            GlStateManager.translate(-0.5F, -0.5F , -0.5F);
        }

        return pair.getLeft();
    }
}
