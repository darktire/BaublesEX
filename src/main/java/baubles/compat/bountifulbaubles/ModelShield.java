package baubles.compat.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelShield extends ModelItem {

    public static final ModelBauble INSTANCE = new ModelShield();

    private ModelShield() {}

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        boolean armor = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        GlStateManager.scale(0.6, 0.6, 0.6);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0.5, -0.25, armor ? 0.7 : 0.75);
        this.renderItem(stack);
    }
}
