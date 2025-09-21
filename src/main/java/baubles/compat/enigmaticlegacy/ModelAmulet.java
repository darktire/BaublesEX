package baubles.compat.enigmaticlegacy;

import baubles.api.model.ModelBauble;
import com.google.common.collect.ImmutableList;
import keletu.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAmulet extends ModelBauble {
    private static final Map<Map.Entry<Item, Integer>, ModelAmulet> instances = new HashMap<>();
    private static final List<String> COLOR_LIST = ImmutableList.<String>builder().add("red", "red", "aqua", "violet", "magenta", "green", "black", "blue").build();
    private static final String DEFAULT_PATH = "textures/models/layer/amulet_red.png";
    private final ResourceLocation texture;
    private final ModelRenderer bipedBody;
    private final ModelRenderer gem;

    public ModelAmulet(Item item, int meta) {
        this.item = item;
        this.model = new keletu.enigmaticlegacy.client.ModelAmulet(ItemStack.EMPTY);
        try {
            this.bipedBody = this.getInner("bipedBody");
            this.gem = this.getInner("gem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.texture = switchTex(item, meta);
    }

    private ModelRenderer getInner(String name) throws Exception {
        Field f = this.model.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return (ModelRenderer) f.get(this.model);
    }

    public static ModelAmulet instance(Item item, int meta) {
        Map.Entry<Item, Integer> pair = new AbstractMap.SimpleEntry<>(item, meta);
        ModelAmulet model = instances.get(pair);
        if (model == null) {
            model = new ModelAmulet(item, meta);
            instances.put(pair, model);
        }
        return model;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        this.render(ageInTicks, scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.render(ageInTicks, scale);
    }

    private void render(float ageInTicks, float scale) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(0.0F, -0.02F, 0.0F);
        GlStateManager.scale(1.1666666F, 1.1666666F, 1.1666666F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bipedBody.render(scale);
        GlStateManager.translate(-0.15625, 0.375, -0.28125);
        if (this.item == EnigmaticLegacy.eldritchAmulet) {
            float lastLightmapX = OpenGlHelper.lastBrightnessX;
            float lastLightmapY = OpenGlHelper.lastBrightnessY;
            float light = 2.0F * (ageInTicks % 100.0F);
            if (ageInTicks % 200.0F < 100.0F) {
                light = 200.0F - light;
            }

            light += 40.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light, light);
            this.gem.render(scale);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightmapX, lastLightmapY);
        } else {
            this.gem.render(scale);
        }

        GlStateManager.popMatrix();
    }

    private static ResourceLocation switchTex(Item item, int meta) {
        if (item == EnigmaticLegacy.enigmaticAmulet) {
            String path = DEFAULT_PATH;
            if (1 <= meta && meta < 8) {
                String color = COLOR_LIST.get(meta);
                path = DEFAULT_PATH.replace("red", color);
            }

            return new ResourceLocation(EnigmaticLegacy.MODID, path);
        }
        else if (item == EnigmaticLegacy.ascensionAmulet) {
            return Resources.FLAME_TEXTURE;
        }
        else if (item == EnigmaticLegacy.eldritchAmulet) {
            return Resources.THORN_TEXTURE;
        }
        return null;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }
}
