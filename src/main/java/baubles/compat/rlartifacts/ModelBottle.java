package baubles.compat.rlartifacts;

import artifacts.client.model.ModelBottledCloud;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

class ModelBottle extends ModelArtifacts {
    public ModelBottle(ResourceLocation tex) {
        super(new ModelBottledCloud(), tex);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        boolean hasPants = !((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.1666666F, 1.1666666F, 1.1666666F);
        GlStateManager.scale(1.0F, 1.0F, hasPants ? 1.2F : 1.1F);
        ((ModelBottledCloud) this.model).belt.render(scale);
        GlStateManager.scale(1.0F, 1.0F, hasPants ? 0.8333333F : 0.9090909F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.translate(0.0F, 1.0F, -0.6666667F);
        GlStateManager.translate(0.2F, 0.0F, 0.0F);
        GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
        ((ModelBottledCloud) this.model).jar.render(scale);
        ((ModelBottledCloud) this.model).lid.render(scale);
        GlStateManager.popMatrix();
    }
}
