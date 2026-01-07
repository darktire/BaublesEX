package baubles.compat.bountifulbaubles;

import baubles.client.model.ModelInherit;
import cursedflames.bountifulbaubles.item.ModItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelAmulet extends ModelInherit {

    public ModelAmulet(ItemStack stack) {
        super(new ModelBiped(), switchTex(stack.getItem()));
    }

    private static ResourceLocation switchTex(Item item) {
        if (item == ModItems.amuletCross) return Resources.CROSS_TEXTURE;
        else if (item == ModItems.sinPendantEmpty) return Resources.EMPTY_TEXTURE;
        else if (item == ModItems.sinPendantGluttony) return Resources.GLUTTONY_TEXTURE;
        else if (item == ModItems.sinPendantPride) return Resources.PRIDE_TEXTURE;
        else if (item == ModItems.sinPendantWrath) return Resources.WRATH_TEXTURE;
        return null;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ((ModelBiped) model).bipedBody.render(scale);
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(0.0F, -0.02F, -0.045F);
            GlStateManager.scale(1.1F, 1.1F, 1.1F);
        }

        final float s = 1.14F;
        GlStateManager.scale(s, s, s);

        ((ModelBiped) model).bipedBody.render(scale);
    }
}
