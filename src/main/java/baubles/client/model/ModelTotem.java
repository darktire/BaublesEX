package baubles.client.model;

import baubles.api.model.ModelBauble;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelTotem extends ModelItem {

    public static final ModelBauble INSTANCE = new ModelTotem();

    private ModelTotem() {}

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        boolean chest = !entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        GlStateManager.scale(0.25, 0.25, 0.25);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0.0, -0.5, chest ? -0.8 : -0.5);
        this.renderItem(stack);
    }
}
