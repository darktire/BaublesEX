package baubles.compat.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelCharm extends ModelItem {

    public static final ModelBauble INSTANCE = new ModelCharm();

    private ModelCharm() {}

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        boolean armor = !entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        GlStateManager.translate(0.15, 0.3, armor ? -0.1875 : -0.125);
        float s = 0.125F;
        GlStateManager.scale(s, s, s);
        GlStateManager.rotate(180, 1, 0, 0);
        this.renderItem(stack);
    }
}
