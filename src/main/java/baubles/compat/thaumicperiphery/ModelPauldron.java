package baubles.compat.thaumicperiphery;

import baubles.client.model.ModelInherit;
import goblinbob.mobends.core.util.BenderHelper;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumicperiphery.ModContent;

public class ModelPauldron extends ModelInherit {

    private ModelPauldron(Item item) {
        super(new thaumicperiphery.render.ModelPauldron(0.125F), switchTex(item));
    }

    private static ResourceLocation switchTex(Item item) {
        if (item == ModContent.pauldron) return Resources.PAULDRON;
        else if (item == ModContent.pauldron_repulsion) return Resources.PAULDRON_REPULSION;
        return null;
    }

    public static class Body extends ModelPauldron{

        Body(Item item) {
            super(item);
        }

        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            ((ModelBiped) this.model).bipedBody.render(scale);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ((ModelBiped) this.model).bipedBody.render(scale);
        }
    }

    public static class Arm extends ModelPauldron{

        Arm(Item item) {
            super(item);
        }

        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            if (FLAG) {
                if (entity.isSneaking()) GlStateManager.translate(0, 0.2F, 0);
                PlayerData data = BenderHelper.getData((AbstractClientPlayer) entity, renderPlayer);
                data.rightArm.applyCharacterTransform(scale);
            }
            else renderPlayer.getMainModel().bipedRightArm.postRender(scale);
            GlStateManager.translate(5 * scale, -4 * scale, 0);
            ((ModelBiped) this.model).bipedRightArm.render(scale);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ((ModelBiped) this.model).bipedRightArm.render(scale);
        }
    }
}
