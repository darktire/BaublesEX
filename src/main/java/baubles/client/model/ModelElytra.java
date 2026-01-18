package baubles.client.model;

import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelElytra extends ModelInherit {
    public static ModelElytra INSTANCE = new ModelElytra();
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("minecraft", "textures/entity/elytra.png");

    public ModelElytra() {
        super(new net.minecraft.client.model.ModelElytra(), null);
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (FLAG && entity instanceof AbstractClientPlayer) {
            PlayerData data = BenderHelper.getData((AbstractClientPlayer) entity, renderPlayer);
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
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        if (entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;

            if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                return abstractclientplayer.getLocationElytra();
            }
            else if (abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                return abstractclientplayer.getLocationElytra();
            }
            else {
                return TEXTURE_ELYTRA;
            }
        }
        else {
            return TEXTURE_ELYTRA;
        }
    }
}
