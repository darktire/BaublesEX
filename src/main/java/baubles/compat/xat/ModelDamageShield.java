package baubles.compat.xat;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.util.TrinketsConfig;

public class ModelDamageShield extends ModelItem {

    public static final ModelBauble INSTANCE = new ModelDamageShield();

    private ModelDamageShield() {}

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (!TrinketsConfig.CLIENT.items.DAMAGE_SHIELD.doRender) return;
        final float offsetX = 0.17F;
        final float offsetY = 0.22F;
        final float offsetZ = 0.16F;
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        GlStateManager.translate(offsetX, -offsetY, offsetZ);
        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(offsetX - 0.14F, 0, -(offsetZ - 0.2F));
        }
        final float bS = 3f;
        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        this.renderItem(stack);
    }
}
