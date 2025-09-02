package baubles.client.render;

import baubles.api.BaublesApi;
import baubles.api.IWrapper;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.event.BaublesRenderEvent;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
    private final RenderPlayer renderPlayer;
    private final ModelPlayer modelPlayer;
    private final boolean slim;

    public BaublesRenderLayer(RenderPlayer renderPlayer, boolean slim) {
        this.renderPlayer = renderPlayer;
        this.modelPlayer = renderPlayer.getMainModel();
        this.slim = slim;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (player.getActivePotionEffect(MobEffects.INVISIBILITY) == null) {
            IBaublesModifiable baubles = BaublesApi.getBaublesHandler((EntityLivingBase) player);
            QueryCtx ctx = new QueryCtx(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            for (int i = 0; i < baubles.getSlots(); i++) {
                if (baubles.getVisible(i)) {
                    ItemStack stack = baubles.getStackInSlot(i);
                    IWrapper wrapper = BaublesApi.toBauble(stack);
                    if (wrapper != null) {
                        ctx.setStack(stack);
                        ctx.setWrapper(wrapper);
                        BaublesRenderEvent.InBaubles event = new BaublesRenderEvent.InBaubles(player, this.slim, stack, i);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) continue;
                        this.renderLayer(ctx);
                    }
                }
            }
            for (EntityEquipmentSlot slotIn : EntityEquipmentSlot.values()) {
                if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    ItemStack stack = player.getItemStackFromSlot(slotIn);
                    IWrapper wrapper = BaublesApi.toBauble(stack);
                    if (wrapper != null) {
                        ctx.setStack(stack);
                        ctx.setWrapper(wrapper);
                        BaublesRenderEvent.InEquipments event = new BaublesRenderEvent.InEquipments(player, this.slim, stack, slotIn);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) continue;
                        this.renderLayer(ctx);
                    }
                }
            }
        }
    }

    private void renderLayer(QueryCtx ctx) {
        Map<ModelBauble, IRenderBauble.RenderType> collection = ctx.wrapper.getRenderMap(this.slim);
        if (collection == null) {
            ModelBauble model = ctx.wrapper.getModel(this.slim);
            IRenderBauble.RenderType render = ctx.wrapper.getRenderType();
            renderModelPart(ctx, model, render);
        }
        else {
            collection.forEach((model, render) -> renderModelPart(ctx, model, render));
        }
    }

    private void renderModelPart(QueryCtx ctx, ModelBauble model, IRenderBauble.RenderType render) {
        if (model != null && render != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableLighting();
            if (ctx.entity.isSneaking() && model.needLocating()) GlStateManager.translate(0, 0.2F, 0);

            ResourceLocation texture = ctx.wrapper.getTexture(this.slim, ctx.entity);
            if (texture != null) renderEachTexture(ctx, render, texture, model, true);

            texture = ctx.wrapper.getEmissiveMap(this.slim, ctx.entity);
            if (texture != null) {
                float lastSky = OpenGlHelper.lastBrightnessX;
                float lastBlock = OpenGlHelper.lastBrightnessY;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                GlStateManager.disableLighting();
                renderEachTexture(ctx, render, texture, model, false);
                GlStateManager.enableLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastSky, lastBlock);
            }

            if (ctx.stack.isItemEnchanted()) {
                model.renderEnchantedGlint(this.renderPlayer, ctx.entity, model, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderEachTexture(QueryCtx ctx, IRenderBauble.RenderType render, ResourceLocation texture, ModelBauble model, boolean flag) {
        this.switchTex(ctx, texture, flag);
        if (flag && model.needLocating()) this.switchBip(render, ctx.scale);
        model.render(ctx.entity, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale, flag);
    }

    private void switchTex(QueryCtx ctx, ResourceLocation texture, boolean flag) {
        if (texture != null) {
            BaublesRenderEvent.SwitchTexture event = new BaublesRenderEvent.SwitchTexture(ctx.entity, this.slim, ctx.stack, texture, flag);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isChanged()) texture = event.getTexture();
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

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public ModelPlayer getModelPlayer() {
        return this.modelPlayer;
    }

    public RenderPlayer getRenderPlayer() {
        return renderPlayer;
    }

    public static final class QueryCtx {
        private final EntityLivingBase entity;
        private final float limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale;
        private ItemStack stack;
        private IWrapper wrapper;

        private QueryCtx(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.entity = entity;
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.partialTicks = partialTicks;
            this.ageInTicks = ageInTicks;
            this.netHeadYaw = netHeadYaw;
            this.headPitch = headPitch;
            this.scale = scale;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

        public void setWrapper(IWrapper wrapper) {
            this.wrapper = wrapper;
        }
    }
}