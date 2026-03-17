package baubles.client.render;

import baubles.api.AbstractWrapper;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.event.BaublesRenderEvent;
import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import com.github.bsideup.jabel.Desugar;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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
                BaublesRenderEvent event = BaublesRenderEvent.of(ctx.entity, this.renderPlayer, stack, slot.id);
                MinecraftForge.EVENT_BUS.post(event);
                if (!event.isCanceled()) {
                    this.renderLayer(ctx, stack, wrapper);
                }
            }
        }
    }

    private void renderLayer(QueryCtx ctx, ItemStack stack, IRenderBauble renderBauble) {
        List<IRenderBauble> list = renderBauble.getSubRender(stack, ctx.entity, this.renderPlayer);
        if (list != null) {
            list.forEach(render -> renderLayer(ctx, stack, render));
        }
        renderModelPart(ctx, stack, renderBauble);
    }

    private void renderModelPart(QueryCtx ctx, ItemStack stack, IRenderBauble renderBauble) {
        ModelBauble model = renderBauble.getModel(stack, ctx.entity, this.renderPlayer);
        IRenderBauble.RenderType render = renderBauble.getRenderType(stack, ctx.entity, this.renderPlayer);
        if (model != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.enableLighting();
            if (ctx.entity.isSneaking() && render != null) GlStateManager.translate(0, 0.2F, 0);

            if (render != null) this.switchBip(render).postRender(ctx.scale);
            model.renderWithTexture(this.renderPlayer, ctx.entity, stack, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale);

            if (model.hasEffect(stack)) {
                model.renderEnchantedGlint(this.renderPlayer, ctx.entity, stack, ctx.limbSwing, ctx.limbSwingAmount, ctx.partialTicks, ctx.ageInTicks, ctx.netHeadYaw, ctx.headPitch, ctx.scale);
            }

            GlStateManager.popMatrix();
        }
    }

    private ModelRenderer switchBip(IRenderBauble.RenderType render) {
        return switch (render) {
            case HEAD -> renderPlayer.getMainModel().bipedHead;
            case BODY -> renderPlayer.getMainModel().bipedBody;
            case ARM_LEFT -> renderPlayer.getMainModel().bipedLeftArm;
            case ARM_RIGHT -> renderPlayer.getMainModel().bipedRightArm;
            case LEG_LEFT -> renderPlayer.getMainModel().bipedLeftLeg;
            case LEG_RIGHT -> renderPlayer.getMainModel().bipedRightLeg;
            case HEAD_WEAR -> renderPlayer.getMainModel().bipedHeadwear;
            case BODY_WEAR -> renderPlayer.getMainModel().bipedBodyWear;
            case ARM_LEFT_WEAR -> renderPlayer.getMainModel().bipedLeftArmwear;
            case ARM_RIGHT_WEAR -> renderPlayer.getMainModel().bipedRightArmwear;
            case LEG_LEFT_WEAR -> renderPlayer.getMainModel().bipedLeftLegwear;
            case LEG_RIGHT_WEAR -> renderPlayer.getMainModel().bipedRightLegwear;
        };
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

    @Desugar
    private record QueryCtx(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}

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