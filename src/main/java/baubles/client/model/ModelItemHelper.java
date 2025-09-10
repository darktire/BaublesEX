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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelItemHelper extends ModelBauble {
    protected final ItemStack stack;
    protected static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    protected final IBakedModel model;
    public ModelItemHelper(Item item, int meta) {
        this.item = item;
        this.stack = new ItemStack(item, 1, meta);
        this.model = itemRender.getItemModelWithOverrides(this.stack, null, null);
    }

    public ModelItemHelper(Item item) {
        this(item, 0);
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        this.renderItem();
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderItemGlint();
    }

    protected void renderItem() {
        itemRender.renderItem(this.stack, this.model);
    }

    protected void renderItemGlint() {
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        GlStateManager.enableBlend();
        ((AccessorRenderItem) itemRender).renderItemGlint(this.model);
        GlStateManager.disableBlend();
    }

    public static ResourceLocation getTexture() {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
