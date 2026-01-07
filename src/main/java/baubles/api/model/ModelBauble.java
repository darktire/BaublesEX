package baubles.api.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class ModelBauble extends ModelBase {

    /**
     * Called by baubles render system.
     */
    public abstract void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag);

    /**
     * This method will be called by {@link LayerArmorBase#renderEnchantedGlint}.
     * You must have a basic model or rewrite this method without bind texture.
     */
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}

    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        LayerArmorBase.renderEnchantedGlint(renderPlayer, entity, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return null;
    }

    public ResourceLocation getEmissiveMap(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return null;
    }

    public boolean needLocating() {
        return true;
    }
}
