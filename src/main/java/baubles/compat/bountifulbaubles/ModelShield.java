package baubles.compat.bountifulbaubles;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ModelShield extends ModelItemHelper {
    private static final Map<Item, ModelShield> instances = new HashMap<>();
    private final Map<EntityLivingBase, Boolean> armor = new HashMap<>();

    public ModelShield(Item item) {
        super(item);
    }

    public static ModelShield instance(Item item) {
        ModelShield model = instances.get(item);
        if (model == null) {
            model = new ModelShield(item);
            instances.put(item, model);
        }
        return model;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        this.armor.put(entity, !entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty());
        GlStateManager.scale(0.6, 0.6, 0.6);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0.5, -0.25, this.armor.get(entity) ? 0.75 : 0.7);
        this.renderItem();
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderItemGlint();
    }
}
