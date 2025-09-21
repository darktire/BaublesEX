package baubles.compat.enigmaticlegacy;

import baubles.api.BaublesApi;
import baubles.client.model.ModelItemHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelScroll extends ModelItemHelper {
    private static final Map<Item, ModelScroll> instances = new HashMap<>();
    private static final Map<EntityLivingBase, List<Item>> map = new HashMap<>();
    private static final Map<EntityLivingBase, Float> perItem = new HashMap<>();
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
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        float rotateAngleY = ((float)entity.ticksExisted + partialTicks) / 5.0F;
        GlStateManager.scale(0.3, 0.3, 0.3);
        GlStateManager.translate(0.0, 0.75, 0.0);
        GlStateManager.rotate(rotateAngleY * 57.295776F + getAngle(entity), 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 2.5F);
        this.renderItem();
    }

    private float getAngle(EntityLivingBase entity) {
        return map.get(entity).indexOf(this.item) * perItem.get(entity);
    }

    public static void addItem(EntityLivingBase entity, Item item) {
        List<Item> scrolls = map.get(entity);
        if (scrolls == null) scrolls = new ArrayList<>();
        scrolls.add(item);
        map.put(entity, scrolls);
        perItem.put(entity, 360F / scrolls.size());
    }

    public static void removeItem(EntityLivingBase entity, Item item) {
        List<Item> scrolls = map.get(entity);
        if (scrolls != null) {
            scrolls.remove(item);
            if (scrolls.contains(item)) {
                perItem.put(entity, 360F / scrolls.size());
            }
            else {
                BaublesApi.log.error("{} is not equipped", item.getRegistryName());
            }
        }
    }
}
