package baubles.client.model;

import baubles.api.model.ModelBauble;
import baubles.proxy.ClientProxy;
import baubles.util.ModsHelper;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ModelElytra extends ModelBauble {
    private static ModelElytra instance;
    private final RenderPlayer renderPlayer;
    private static final boolean FLAG = ModsHelper.isModLoaded("mobends");

    public ModelElytra(boolean slim) {
        this.model = new net.minecraft.client.model.ModelElytra();
        this.renderPlayer = slim ? ClientProxy.SLIM_LAYER.getRenderPlayer() : ClientProxy.NORMAL_LAYER.getRenderPlayer();
    }

    public static ModelElytra instance(boolean slim) {
        if (instance == null) {
            instance = new ModelElytra(slim);
        }
        return instance;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (FLAG && entity instanceof AbstractClientPlayer) {
            PlayerData data = BenderHelper.getData((AbstractClientPlayer) entity, this.renderPlayer);
            assert data != null;
            data.body.applyCharacterTransform(0.0625F);
            GlStateManager.translate(0.0F, -12.0F * scale, 0.0F);
        }
        else {
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
        }
        this.model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean needLocating() {
        return false;
    }
}
