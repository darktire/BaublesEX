package baubles.api.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;

public class ModelBauble extends ModelBase {
    protected Item item;
    protected boolean slim;
    protected ModelBase model;

    public ModelBauble() {}

    public ModelBauble(Item item, boolean slim) {
        this.item = item;
        this.slim = slim;
    }

    public ModelBauble(ModelBase model) {
        this.model = model;
    }

    /**
     * Called by baubles render system.
     */
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (this.model == null) return;
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    /**
     * This method will be called by {@link LayerArmorBase#renderEnchantedGlint}.
     * You must have a basic model or rewrite this method without bind texture.
     */
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (this.model == null) return;
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        LayerArmorBase.renderEnchantedGlint(renderPlayer, entity, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public boolean needLocating() {
        return true;
    }
}
