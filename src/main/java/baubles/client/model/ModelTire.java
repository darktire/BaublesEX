package baubles.client.model;

import baubles.BaublesRegister;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ModelTire extends ModelItemHelper {
    private static ModelTire instance;

    public ModelTire() {
        super(BaublesRegister.ModItems.tire);
    }

    public static ModelTire instance() {
        if (instance == null) {
            instance = new ModelTire();
        }
        return instance;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        GlStateManager.translate(0, 0, 0.5);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) / 2, 0, 0, 1);
        GlStateManager.scale(3, 3, 1);
        this.renderItem();
    }
}
