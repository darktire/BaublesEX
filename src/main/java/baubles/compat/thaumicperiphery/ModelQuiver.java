package baubles.compat.thaumicperiphery;

import baubles.client.model.ModelInherit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import thaumcraft.api.items.RechargeHelper;

public class ModelQuiver extends ModelInherit {
    public ModelQuiver() {
        super(new Edit(), Resources.getLoc("magic_quiver.png"));
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        float fill = RechargeHelper.getChargePercentage(stack, (EntityPlayer) entity);
        ((Edit) this.model).render1(fill, scale, true);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ((Edit) this.model).render1(1, scale, false);
    }

    private static class Edit extends thaumicperiphery.render.ModelQuiver {
        private Edit() {
            super(0.03125F);
        }

        private void render1(float fill, float scale, boolean flag) {
            this.bipedBody.render(scale);
            this.quiver.render(scale);
            if (flag) {
                Minecraft.getMinecraft().renderEngine.bindTexture(Resources.ARROW_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.6F);
                GlStateManager.enableBlend();
            }
            int numArrows = MathHelper.ceil(fill * (float)this.arrows.length);

            for(int i = 0; i < numArrows; ++i) {
                this.arrows[i].render(scale);
            }

            if (flag) {
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
