package baubles.client.render;

import baubles.api.BaublesApi;
import baubles.api.IWrapper;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.event.BaublesRenderEvent;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.Map;

public final class BaublesRenderLayer implements LayerRenderer<EntityPlayer> {

    private final ModelPlayer modelPlayer;
    private final boolean slim;

    public BaublesRenderLayer(ModelPlayer modelPlayer, boolean slim) {
        this.modelPlayer = modelPlayer;
        this.slim = slim;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (player.getActivePotionEffect(MobEffects.INVISIBILITY) == null) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            for (int i = 0; i < baubles.getSlots(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    BaublesRenderEvent event = new BaublesRenderEvent(player, this.slim, stack, true, i);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) continue;
                    this.renderLayer(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, stack);
                }
            }
            for (EntityEquipmentSlot slotIn : EntityEquipmentSlot.values()) {
                if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    ItemStack stack = player.getItemStackFromSlot(slotIn);
                    if (!stack.isEmpty()) {
                        BaublesRenderEvent event = new BaublesRenderEvent(player, this.slim, stack, false, slotIn);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) continue;
                        this.renderLayer(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, stack);
                    }
                }
            }
        }
    }

    private void renderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, ItemStack stack) {
        IWrapper wrapper = BaublesApi.toBauble(stack);
        Map<ModelBase, IRenderBauble.RenderType> collection = wrapper.getRenderMap(this.slim);
        if (collection == null) {
            ModelBase model = wrapper.getModel(this.slim);
            IRenderBauble.RenderType render = wrapper.getRenderType();
            renderPart(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, model, render, wrapper);
        }
        else {
            collection.forEach((model, render) -> renderPart(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, model, render, wrapper));
        }
    }

    private void renderPart(EntityPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, ModelBase model, IRenderBauble.RenderType render, IWrapper wrapper) {
        if (model != null && render != null) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
            if (player.isSneaking()) GlStateManager.translate(0, 0.2F, 0);
            ResourceLocation texture = wrapper.getTexture(this.slim);
            renderEach(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, render, texture, model);
            texture = wrapper.getLuminousTexture(this.slim);
            if (texture != null) {
                float lastLightmapX = OpenGlHelper.lastBrightnessX;
                float lastLightmapY = OpenGlHelper.lastBrightnessY;
                int light = 15728880;
                int lightmapX = light % 65536;
                int lightmapY = light / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
                GlStateManager.disableLighting();
                renderEach(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, render, texture, model);
                GlStateManager.enableLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightmapX, lastLightmapY);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderEach(EntityPlayer player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, IRenderBauble.RenderType render, ResourceLocation texture, ModelBase model) {
        GlStateManager.pushMatrix();
        this.switchTex(texture);
        this.switchBip(render, scale);
        model.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();
    }

    private void switchTex(ResourceLocation texture) {
        if (texture != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        }
    }

    private void switchBip(IRenderBauble.RenderType render, float scale) {
        switch (render) {
            case HEAD:
                modelPlayer.bipedHead.postRender(scale); break;
            case BODY:
                modelPlayer.bipedBody.postRender(scale); break;
            case ARM_LEFT:
                modelPlayer.bipedLeftArm.postRender(scale); break;
            case ARM_RIGHT:
                modelPlayer.bipedRightArm.postRender(scale); break;
            case LEG_LEFT:
                modelPlayer.bipedLeftLeg.postRender(scale); break;
            case LEG_RIGHT:
                modelPlayer.bipedRightLeg.postRender(scale); break;
        }
    }

    public ModelPlayer getModelPlayer() {
        return this.modelPlayer;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
//todo switcher for render