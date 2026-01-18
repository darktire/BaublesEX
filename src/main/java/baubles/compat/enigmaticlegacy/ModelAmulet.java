package baubles.compat.enigmaticlegacy;

import baubles.client.model.ModelInherit;
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

public class ModelAmulet extends ModelInherit {
    private static final String[] COLOR_LIST = new String[]{"red", "red", "aqua", "violet", "magenta", "green", "black", "blue"};
    private static final String DEFAULT_PATH = "textures/models/layer/amulet_red.png";
    private final boolean flag;
    private final ModelRenderer bipedBody;
    private final ModelRenderer gem;

    public ModelAmulet(ItemStack stack) {
        super(new Edited(stack), switchTex(stack));
        this.flag = stack.getItem() == EnigmaticLegacy.eldritchAmulet;
        this.bipedBody = ((Edited) this.model).getInner(true);
        this.gem = ((Edited) this.model).getInner(false);
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
        if (this.flag) {
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

    private static ResourceLocation switchTex(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        if (item == EnigmaticLegacy.enigmaticAmulet) {
            String path = DEFAULT_PATH;
            if (1 <= meta && meta < 8) {
                String color = COLOR_LIST[meta];
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

    private static class Edited extends keletu.enigmaticlegacy.client.ModelAmulet {
        public Edited(ItemStack stack) {
            super(stack);
        }

        public ModelRenderer getInner(boolean b) {
            if (b) return this.bipedBody;
            else return this.gem;
        }
    }
}
