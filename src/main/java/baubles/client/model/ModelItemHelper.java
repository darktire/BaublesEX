package baubles.client.model;

import baubles.api.model.ModelBauble;
import baubles.mixin.early.vanilla.AccessorRenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModelItemHelper extends ModelBauble {
    protected final ItemStack stack;
    protected static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    protected final IBakedModel model;
    private static final ResourceLocation TEXTURES = TextureMap.LOCATION_BLOCKS_TEXTURE;
    public ModelItemHelper(Item item) {
        this.stack = new ItemStack(item);
        this.model = itemRender.getItemModelWithOverrides(this.stack, null, null);
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

    public ResourceLocation getTexture() {
        return TEXTURES;
    }
}
