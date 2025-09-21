package baubles.compat.xat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.api.render.IRenderBauble;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import java.util.HashMap;
import java.util.Map;

public class ModelClaws extends ModelBauble {
    private static final Map<Boolean, ModelClaws> instances = new HashMap<>();
    private ModelClaws copy;
    private boolean isCopy = false;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/claws.png");

    public ModelClaws(boolean slim) {
        super(ModItems.baubles.BaubleFaelisClaw, slim);
    }

    public static ModelClaws instance(boolean slim) {
        ModelClaws model = instances.get(slim);
        if (model == null) {
            model = new ModelClaws(slim);
            instances.put(slim, model);
        }
        return model;
    }

    ModelClaws copy() {
        if (this.copy == null) {
            this.copy = new ModelClaws(this.slim);
            this.copy.isCopy = true;
        }
        return this.copy;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (!TrinketsConfig.CLIENT.items.FAELIS_CLAW.doRender) {
            return;
        }

        final float offsetX = this.slim ? 12.4F : 18.6F;
        final float offsetY = 61F;
        final float offsetZ = -21F;
        final float bS = 0.16f;

        GlStateManager.scale(scale * bS, scale * bS, scale * bS);
        GlStateManager.translate((isCopy ? -1 : 1) * offsetX, offsetY, offsetZ);
        GlStateManager.rotate(-90F, 0F, 1F, 0F);
        DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);

    }

    public static ResourceLocation getTexture() {
        return TEXTURE;
    }

    public static Map<ModelBauble, IRenderBauble.RenderType> getRenderMap(EntityLivingBase entity, boolean slim) {
        if (!TrinketsConfig.SERVER.Items.FAELIS_CLAW.compat.baubles.equip_multiple) {
            return RenderMap.of(slim).init().both;
        }
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        int j = baubles.indexOf(TypesData.Preset.RING, 0);
        int k = baubles.indexOf(ModItems.baubles.BaubleFaelisClaw, 0);
        if (k != -1) {
            int l = baubles.indexOf(ModItems.baubles.BaubleFaelisClaw, k);
            if (l != k) return RenderMap.of(slim).init().both;
        }
        if (((k - j) & 1) == 0) {
            return RenderMap.of(slim).init().r;
        }
        else {
            return RenderMap.of(slim).init().l;
        }
    }

    private enum RenderMap {
        NORMAL(false), SLIM(true);

        private final boolean slim;
        Map<ModelBauble, IRenderBauble.RenderType> both;
        Map<ModelBauble, IRenderBauble.RenderType> l;
        Map<ModelBauble, IRenderBauble.RenderType> r;

        RenderMap(boolean slim) {
            this.slim = slim;
        }

        private RenderMap init() {
            if (this.both == null || this.l == null || this.r == null) {
                ModelClaws model = ModelClaws.instance(this.slim);
                ModelClaws copy1 = model.copy();
                this.both = ImmutableMap.of(
                        model, IRenderBauble.RenderType.ARM_LEFT,
                        copy1, IRenderBauble.RenderType.ARM_RIGHT
                );
                this.l = ImmutableMap.of(model, IRenderBauble.RenderType.ARM_LEFT);
                this.r = ImmutableMap.of(copy1, IRenderBauble.RenderType.ARM_RIGHT);
            }
            return this;
        }

        private static RenderMap of(boolean slim) {
            return slim ? SLIM : NORMAL;
        }
    }
}
