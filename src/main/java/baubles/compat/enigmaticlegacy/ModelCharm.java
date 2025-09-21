package baubles.compat.enigmaticlegacy;

import baubles.client.model.ModelItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModelCharm extends ModelItemHelper {
    private static final Map<Map.Entry<Item, Integer>, ModelCharm> instances = new HashMap<>();

    public ModelCharm(Item item, int meta) {
        super(item, meta);
    }

    public static ModelCharm instance(Item item, int meta) {
        Map.Entry<Item, Integer> pair = new AbstractMap.SimpleEntry<>(item, meta);
        ModelCharm model = instances.get(pair);
        if (model == null) {
            model = new ModelCharm(item, meta);
            instances.put(pair, model);
        }
        return model;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        boolean armor = !entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty();
        GlStateManager.translate(0.15, 0.3, armor ? -0.1875 : -0.125);
        float s = 0.125F;
        GlStateManager.scale(s, s, s);
        GlStateManager.rotate(180, 1, 0, 0);
        this.renderItem();
    }
}
