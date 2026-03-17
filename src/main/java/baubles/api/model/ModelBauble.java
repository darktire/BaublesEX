package baubles.api.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class ModelBauble extends ModelBase {
    
    public void renderWithTexture(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ResourceLocation texture = getTexture(stack, entity, renderPlayer);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        this.render(renderPlayer, entity, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    /**
     * Called by {@link ModelBauble#renderWithTexture}.
     * If there is no texture, use {@link NoTex}.
     */
    public abstract void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    /**
     * This method will be called by {@link LayerArmorBase#renderEnchantedGlint}.
     * You must have a basic model or rewrite this method without bind texture.
     */
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}

    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        LayerArmorBase.renderEnchantedGlint(renderPlayer, entity, this, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    /**
     * Called by {@link ModelBauble#renderWithTexture}.
     */
    public abstract ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer);

    public boolean hasEffect(ItemStack stack) {
        return stack.hasEffect();
    }

    public abstract static class NoTex extends ModelBauble {

        @Override
        public void renderWithTexture(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.render(renderPlayer, entity, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }

        @Override
        public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
            return null;
        }
    }

    protected static RenderItem getItemRender() {
        return Minecraft.getMinecraft().getRenderItem();
    }

    protected static void lightThis(Runnable task) {
        float lastSky = OpenGlHelper.lastBrightnessX;
        float lastBlock = OpenGlHelper.lastBrightnessY;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        GlStateManager.disableLighting();
        task.run();
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastSky, lastBlock);
    }
}
