package baubles.compat.enigmaticlegacy;

import baubles.client.model.ModelItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ModelScroll extends ModelItem {
    public ModelScroll() {}

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        float rotateAngleY = ((float)entity.ticksExisted + partialTicks) / 5.0F;
        GlStateManager.scale(0.3, 0.3, 0.3);
        GlStateManager.translate(0.0, 0.75, 0.0);
        GlStateManager.rotate(rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 2.5F);
        this.renderItem(stack);
    }
}
