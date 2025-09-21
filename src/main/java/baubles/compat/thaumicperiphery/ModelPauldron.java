package baubles.compat.thaumicperiphery;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.proxy.ClientProxy;
import baubles.util.ModsHelper;
import com.google.common.collect.ImmutableMap;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class ModelPauldron extends ModelBauble {
    private final ModelBiped model;
    private final ModelPlayer modelPlayer;
    private final RenderPlayer renderPlayer;
    private static final boolean FLAG = ModsHelper.isModLoaded("mobends");

    private ModelPauldron(boolean slim) {
        super(new thaumicperiphery.render.ModelPauldron(0.125F));
        this.model = (ModelBiped) super.model;
        this.modelPlayer = slim ? ClientProxy.SLIM_LAYER.getModelPlayer() : ClientProxy.NORMAL_LAYER.getModelPlayer();
        this.renderPlayer = slim ? ClientProxy.SLIM_LAYER.getRenderPlayer() : ClientProxy.NORMAL_LAYER.getRenderPlayer();
    }

    public static Map<ModelBauble, IRenderBauble.RenderType> getRenderMap(boolean slim) {
        return RenderMap.of(slim).init().map;
    }

    private enum RenderMap {
        SLIM(true), NORMAL(false);
        private ImmutableMap<ModelBauble, IRenderBauble.RenderType> map;
        private final boolean slim;

        RenderMap(boolean slim) {
            this.slim = slim;
        }

        private RenderMap init() {
            if (map == null) {
                this.map = ImmutableMap.of(
                        new Body(slim), IRenderBauble.RenderType.BODY,
                        new Arm(slim), IRenderBauble.RenderType.ARM_RIGHT
                );
            }
            return this;
        }

        private static RenderMap of(boolean slim) { return slim ? SLIM : NORMAL; }
    }

    public static class Body extends ModelPauldron{

        private Body(boolean slim) {
            super(slim);
        }

        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            super.model.bipedBody.render(scale);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.model.bipedBody.render(scale);
        }
    }

    public static class Arm extends ModelPauldron{

        private Arm(boolean slim) {
            super(slim);
        }

        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            if (FLAG) {
                if (entity.isSneaking()) GlStateManager.translate(0, 0.2F, 0);
                PlayerData data = BenderHelper.getData((AbstractClientPlayer) entity, super.renderPlayer);
                data.rightArm.applyCharacterTransform(scale);
            }
            else super.modelPlayer.bipedRightArm.postRender(scale);
            GlStateManager.translate(5 * scale, -4 * scale, 0);
            super.model.bipedRightArm.render(scale);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.model.bipedRightArm.render(scale);
        }

        @Override
        public boolean needLocating() {
            return false;
        }
    }
}
