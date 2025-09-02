package baubles.compat.bountifulbaubles;

import baubles.api.model.ModelBauble;
import cursedflames.bountifulbaubles.item.ModItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModelAmulet extends ModelBauble {
    private static final Map<Item, ModelAmulet> instances = new HashMap<>();
    private final ResourceLocation texture;

    public ModelAmulet(Item item) {
        this.texture = switchTex(item);
        this.model = new ModelBiped();
    }

    public static ModelAmulet instance(Item item) {
        ModelAmulet model = instances.get(item);
        if (model == null) {
            model = new ModelAmulet(item);
            instances.put(item, model);
        }
        return model;
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
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
            GlStateManager.translate(0.0F, -0.02F, -0.045F);
            GlStateManager.scale(1.1F, 1.1F, 1.1F);
        }

        final float s = 1.14F;
        GlStateManager.scale(s, s, s);

        ((ModelBiped) model).bipedBody.render(scale);
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }
}
