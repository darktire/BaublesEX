package baubles.compat.xat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModelEnderTiara extends ModelXat {
    public static ModelEnderTiara instance = new ModelEnderTiara();

    public ModelEnderTiara() {
        super(ModItems.baubles.BaubleEnderTiara.getRegistryName().toString() + "_model");
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!TrinketsConfig.CLIENT.ITEMS.ENDER_CROWN.RENDER) return;
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
