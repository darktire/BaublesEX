package baubles.client.render;

import baubles.api.AbstractWrapper;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
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
import java.util.Arrays;
import java.util.List;

public final class BaublesRenderLayer implements LayerRenderer<EntityPlayer> {
    private final RenderPlayer renderPlayer;

    public BaublesRenderLayer(RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (player.getActivePotionEffect(MobEffects.INVISIBILITY) == null) {
            QueryCtx ctx = new QueryCtx(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            BaublesApi.applyByIndex(player, (baubles, i) -> renderPerSlots(new BaubleRef(baubles, i), ctx));
            Arrays.stream(EntityEquipmentSlot.values()).forEach(i -> renderPerSlots(new ArmorRef(i), ctx));
        }
    }

    private void renderPerSlots(SlotRef slot, QueryCtx ctx) {
        if (slot.visible()) {
            ItemStack stack = slot.getStack(ctx.entity);
            AbstractWrapper wrapper = BaublesApi.toBauble(stack);
            if (wrapper != null) {
                ctx.setStack(stack);
                BaublesRenderEvent event = BaublesRenderEvent.of(ctx.entity, this.renderPlayer, stack, slot.id);
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.isCanceled()) {
                    this.renderLayer(ctx, wrapper);
                }
            }
        }
    }

    private void renderLayer(QueryCtx ctx, IRenderBauble renderBauble) {
        List<IRenderBauble> list = renderBauble.getSubRender(ctx.stack, ctx.entity, this.renderPlayer);
        if (list != null) {
            list.forEach(render -> renderLayer(ctx, render));
        }
        renderModelPart(ctx, renderBauble);
    }

    private void renderModelPart(QueryCtx ctx, IRenderBauble renderBauble) {
        ModelBauble model = renderBauble.getModel(ctx.stack, ctx.entity, this.renderPlayer);
        IRenderBauble.RenderType render = renderBauble.getRenderType(ctx.stack, ctx.entity, this.renderPlayer);
        if (model != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableLighting();
            if (ctx.entity.isSneaking() && render != null) GlStateManager.translate(0, 0.2F, 0);

            ResourceLocation texture = model.getTexture(ctx.stack, ctx.entity, this.renderPlayer);
            renderEachTexture(ctx, render, texture, model, true);

            texture = model.getEmissiveMap(ctx.stack, ctx.entity, this.renderPlayer);
            if (texture != null) {
                float lastSky = OpenGlHelper.lastBrightnessX;
                float lastBlock = OpenGlHelper.lastBrightnessY;

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
                GlStateManager.disableLighting();
                renderEachTexture(ctx, render, texture, model, false);
                GlStateManager.enableLighting();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastSky, lastBlock);
            }

            if (ctx.stack.hasEffect()) {
                model.renderEnchantedGlint(this.renderPlayer, ctx.entity, ctx.stack, model, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale);
            }

            GlStateManager.popMatrix();
        }
    }

    private void renderEachTexture(QueryCtx ctx, IRenderBauble.RenderType render, ResourceLocation texture, ModelBauble model, boolean ems) {
        this.switchTex(ctx, texture, ems);
        if (ems && render != null) this.switchBip(render, ctx.scale);
        model.render(this.renderPlayer, ctx.entity, ctx.stack, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale, ems);
    }

    private void switchTex(QueryCtx ctx, ResourceLocation texture, boolean ems) {
        if (texture != null) {
            BaublesRenderEvent.SwitchTexture event = new BaublesRenderEvent.SwitchTexture(ctx.entity, this.renderPlayer, ctx.stack, texture, ems);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isChanged()) texture = event.getTexture();
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        }
    }

    private void switchBip(IRenderBauble.RenderType render, float scale) {
        switch (render) {
            case HEAD:
                renderPlayer.getMainModel().bipedHead.postRender(scale); break;
            case BODY:
                renderPlayer.getMainModel().bipedBody.postRender(scale); break;
            case ARM_LEFT:
                renderPlayer.getMainModel().bipedLeftArm.postRender(scale); break;
            case ARM_RIGHT:
                renderPlayer.getMainModel().bipedRightArm.postRender(scale); break;
            case LEG_LEFT:
                renderPlayer.getMainModel().bipedLeftLeg.postRender(scale); break;
            case LEG_RIGHT:
                renderPlayer.getMainModel().bipedRightLeg.postRender(scale); break;
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public ModelPlayer getModelPlayer() {
        return this.renderPlayer.getMainModel();
    }

    public RenderPlayer getRenderPlayer() {
        return renderPlayer;
    }

    private static final class QueryCtx {
        private final EntityLivingBase entity;
        private final float limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale;
        private ItemStack stack;

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
    }

    private static abstract class SlotRef {
        private final Object id;
        private SlotRef(Object id) { this.id = id; }
        protected abstract ItemStack getStack(EntityLivingBase e);
        protected abstract boolean visible();
    }

    private static final class BaubleRef extends SlotRef {
        private final IBaublesItemHandler baubles;
        private final int index;
        private BaubleRef(IBaublesItemHandler baubles, int index) {
            super(index);
            this.baubles = baubles;
            this.index = index;
        }
        @Override
        protected ItemStack getStack(EntityLivingBase p) {
            return this.baubles.getStackInSlot(index);
        }
        @Override
        protected boolean visible() {
            return this.baubles.getVisible(index);
        }
    }

    private static final class ArmorRef extends SlotRef {
        private final EntityEquipmentSlot slot;
        private ArmorRef(EntityEquipmentSlot slotIn) {
            super(slotIn);
            this.slot = slotIn;
        }
        @Override
        protected ItemStack getStack(EntityLivingBase e) {
            return e.getItemStackFromSlot(slot);
        }
        @Override
        protected boolean visible() {
            return slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR;
        }
    }
}