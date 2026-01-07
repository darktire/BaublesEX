package baubles.client.model;

import baubles.api.model.ModelBauble;
import baubles.mixin.early.vanilla.AccessorRenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.ref.WeakReference;

public class ModelItem extends ModelBauble {
    protected static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    protected WeakReference<ItemStack> ref;
    protected IBakedModel model;

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        this.renderItem(stack);
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderItemGlint(stack);
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    protected void renderItem(ItemStack stack) {
        itemRender.renderItem(stack, getModel(stack));
    }

    protected void renderItemGlint(ItemStack stack) {
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.enableBlend();
        ((AccessorRenderItem) itemRender).renderItemGlint(getModel(stack));
        GlStateManager.disableBlend();
    }

    protected IBakedModel getModel(ItemStack stack) {
        if (this.ref == null || isDiff(stack, this.ref.get())) {
            this.ref = new WeakReference<>(stack);
            this.model = itemRender.getItemModelWithOverrides(stack, null, null);
        }
        return this.model;
    }

    private boolean isDiff(ItemStack stack, ItemStack stack1) {
        return stack1 == null || !stack1.isItemEqual(stack);
    }
}
