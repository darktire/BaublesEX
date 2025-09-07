package baubles.compat.enigmaticlegacy;

import baubles.Baubles;
import baubles.client.model.ModelItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelScroll extends ModelItemHelper {
    private static final Map<Item, ModelScroll> instances = new HashMap<>();
    private static final List<Item> list = new ArrayList<>();
    private static float perItem = 0;
    public ModelScroll(Item item) {
        super(item);
    }

    public static ModelScroll instance(Item item) {
        ModelScroll model = instances.get(item);
        if (model == null) {
            model = new ModelScroll(item);
            instances.put(item, model);
        }
        return model;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        float rotateAngleY = ((float)entity.ticksExisted + partialTicks) / 5.0F;
        GlStateManager.scale(0.3, 0.3, 0.3);
        GlStateManager.translate(0.0, 0.75, 0.0);
        GlStateManager.rotate(rotateAngleY * 57.295776F + list.indexOf(this.item) * perItem, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 2.5F);
        this.renderItem();
    }

    public static void addItem(Item item) {
        list.add(item);
        perItem = 360F / list.size();
    }

    public static void removeItem(Item item) {
        if (list.contains(item)) {
            list.remove(item);
            perItem = 360F / list.size();
        }
        else {
            Baubles.log.error(item.getRegistryName() + " is not equipped");
        }
    }
}
