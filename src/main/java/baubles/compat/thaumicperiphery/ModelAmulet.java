package baubles.compat.thaumicperiphery;

import baubles.api.model.ModelBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.lib.UtilsFX;
import thaumicperiphery.render.LayerExtraBaubles;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModelAmulet extends ModelBauble {
    private static final Map<Map.Entry<Item, Boolean>, ModelAmulet> instances = new HashMap<>();
    private final ModelBiped model1;
    private final boolean flag;
    private final int width, height;

    private ModelAmulet(Item item, boolean flag) {
        super(item, false);
        this.model1 = new ModelBiped();
        this.flag = flag;
        this.width = getWidth(flag);
        this.height = getHeight();
    }

    private int getWidth(boolean flag) {
        return this.item == ItemsTC.baubles ? 5 : flag ? 6 : 5;
    }

    private int getHeight() {
        return this.item == ItemsTC.baubles ? 5 : 6;
    }

    public static ModelAmulet instance(Item item, boolean flag) {
        return instances.putIfAbsent(new AbstractMap.SimpleEntry<>(item, flag), new ModelAmulet(item, flag));
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        GlStateManager.translate(0.0F, -5.0E-4F, 0.0F);
        float s = 1.05F;
        GlStateManager.scale(s, s, s);
        this.model1.bipedBody.render(scale);

        GlStateManager.pushMatrix();
        drawIcon(scale);
        GlStateManager.popMatrix();
    }

    private void drawIcon(float s) {
        GlStateManager.scale(s, s, s);
        GlStateManager.scale(0.5F * width, -0.5F * height, 2.5F);
        GlStateManager.translate(-0.5F, -1.0F - (14 - height) / 16.0F, -0.725F);
        if (this.flag) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float minU = LayerExtraBaubles.visAmuletSprite.getMinU();
            float maxU = LayerExtraBaubles.visAmuletSprite.getMaxU();
            float minV = LayerExtraBaubles.visAmuletSprite.getMinV();
            float maxV = LayerExtraBaubles.visAmuletSprite.getMaxV();
            float diffU = maxU - minU;
            float diffV = maxV - minV;
            maxU = minU + diffU * (width / 16.0F);
            maxV = minV + diffV * (height / 16.0F);
            LayerExtraBaubles.renderIconIn3D(Tessellator.getInstance(), minU, minV, maxU, maxV, width, height, 0.25F);
        } else {
            UtilsFX.renderTextureIn3D(1.0F - width / 128.0F, 0.0F, 1.0F, height / 64.0F, width, height, 0.25F);
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
}
