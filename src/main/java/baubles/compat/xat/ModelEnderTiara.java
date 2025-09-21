package baubles.compat.xat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModelEnderTiara extends ModelXat {
    private static ModelEnderTiara instance;
    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation(ModItems.baubles.BaubleEnderTiara.getRegistryName().toString() + "_model");

    public ModelEnderTiara() {
        super(MODEL_LOCATION);
    }

    public static ModelEnderTiara instance() {
        if (instance == null) {
            instance = new ModelEnderTiara();
        }
        return instance;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (!TrinketsConfig.CLIENT.items.ENDER_CROWN.doRender) return;
        boolean hasHelmet = entity.hasItemInSlot(EntityEquipmentSlot.HEAD);
        float hScale = hasHelmet ? 1.0F : 0.85F;
        double helmetOffsetY = hasHelmet ? 0.11 : 0;
        double helmetOffsetZ = hasHelmet ? -0.04 : 0;
        GlStateManager.translate(0.0, 0.45, -0.04);
        GlStateManager.translate(0.0F, helmetOffsetY, helmetOffsetZ);
        GlStateManager.scale(hScale, hScale, hScale);
        this.render();
    }
}
