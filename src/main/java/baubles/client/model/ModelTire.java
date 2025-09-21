package baubles.client.model;

import baubles.BaublesRegister;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

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
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        GlStateManager.translate(0, 0, 0.5);
        GlStateManager.rotate((entity.ticksExisted + partialTicks) / 2, 0, 0, 1);
        GlStateManager.scale(3, 3, 1);
        this.renderItem();
    }
}
