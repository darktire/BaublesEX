package baubles.compat.xat;

import baubles.client.model.ModelItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModelSeaStone extends ModelItemHelper {
    private static ModelSeaStone instance;

    public ModelSeaStone() {
        super(ModItems.baubles.BaubleSea);
    }

    public static ModelSeaStone instance() {
        if (instance == null) {
            instance = new ModelSeaStone();
        }
        return instance;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (!TrinketsConfig.CLIENT.items.SEA_STONE.doRender) return;
        final float offsetY = 0.16F;
        final float offsetZ = 0.14F;
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.translate(0F, -offsetY, offsetZ);
        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(0F, 0, -(offsetZ - 0.2F));
        }
        final float bS = 3f;
        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        this.renderItem();
    }
}
