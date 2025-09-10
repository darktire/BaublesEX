package baubles.compat.xat;

import baubles.api.model.ModelBauble;
import baubles.mixin.early.vanilla.AccessorRenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModelXat extends ModelBauble {
    private static final RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
    private final IBakedModel model;

    public ModelXat(ResourceLocation loc) {
        ModelResourceLocation modelLoc = new ModelResourceLocation(loc, "inventory");
        this.model = renderer.getItemModelMesher().getModelManager().getModel(modelLoc);
    }

    public static ResourceLocation getTexture() {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.enableBlend();
        ((AccessorRenderItem) renderer).renderItemGlint(this.model);
        GlStateManager.disableBlend();
    }

    protected void render() {
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5F, 0.75F, -0.25F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.renderQuads(bufferbuilder, this.model.getQuads(null, enumfacing, 0L));
        }

        this.renderQuads(bufferbuilder, this.model.getQuads(null, null, 0L));
        tessellator.draw();
    }


    protected void renderQuads(BufferBuilder buffer, List<BakedQuad> quads) {
        int i = 0;

        for (int j = quads.size(); i < j; ++i) {
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer,  quads.get(i), -1);
        }
    }
}
