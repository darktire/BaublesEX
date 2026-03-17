package baubles.compat.rlartifacts;

import artifacts.common.init.ModItems;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypeData;
import baubles.client.model.ModelInherit;
import baubles.client.model.Models;
import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModelGlove extends ModelInherit {

    private final Item item;
    private final ResourceLocation emissive;

    private static final Map<Item, ResourceLocation> TEXTURE_MAP = ImmutableMap.<Item, ResourceLocation>builder()
            .put(ModItems.POWER_GLOVE, Resources.POWER_GLOVE_TEXTURE)
            .put(ModItems.FERAL_CLAWS, Resources.FERAL_CLAWS_TEXTURE)
            .put(ModItems.MECHANICAL_GLOVE, Resources.MECHANICAL_GLOVE_TEXTURE)
            .put(ModItems.FIRE_GAUNTLET, Resources.FIRE_GAUNTLET_TEXTURE)
            .put(ModItems.POCKET_PISTON, Resources.POCKET_PISTON_TEXTURE)
            .build();

    private static final Map<Item, ResourceLocation> EMISSIVE_MAP = ImmutableMap.<Item, ResourceLocation>builder()
            .put(ModItems.FIRE_GAUNTLET, Resources.FIRE_GAUNTLET_OVERLAY_TEXTURE)
            .put(ModItems.MAGMA_STONE, Resources.FIRE_GAUNTLET_OVERLAY_TEXTURE)
            .build();

    public ModelGlove(Models.Key key) {
        super(key.renderPlayer().getMainModel(), null);
        this.item = key.item();
        this.texture = resolveTexture(TEXTURE_MAP.get(item), key.renderPlayer().smallArms);
        this.emissive = resolveTexture(EMISSIVE_MAP.get(item), key.renderPlayer().smallArms);
    }

    public static ModelBauble getInstance(ItemStack stack, RenderPlayer renderPlayer) {
        return Models.getInstance(Models.wrap(stack.getItem(), stack.getMetadata(), renderPlayer), ModelGlove::new);
    }

    private static ResourceLocation resolveTexture(ResourceLocation base, boolean slim) {
        if (base == null || !slim) return base;
        return new ResourceLocation(base.toString().replace("normal", "slim"));
    }

    @Override
    public void renderWithTexture(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (this.texture != null) {
            super.renderWithTexture(renderPlayer, entity, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (this.emissive != null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.emissive);
            lightThis(() -> this.render(renderPlayer, entity, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale));
        }
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        renderGlove((EntityLivingBase) entity, scale);
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.isSneaking()) {
            GlStateManager.translate(0, 0.2F, 0);
        }
        renderGlove(entity, scale);
    }

    private void renderGlove(EntityLivingBase entity, float scale) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        ModelPlayer model = (ModelPlayer) this.model;

        int i = baubles.indexOf(this.item, 0);
        if (i == -1) return;

        if (baubles.indexOf(this.item, i + 1) != -1) {
            model.bipedLeftArm.render(scale);
            model.bipedRightArm.render(scale);
            return;
        }

        int j = baubles.indexOf(TypeData.Preset.RING, 0);
        if (((i - j) & 1) == 0) {
            model.bipedRightArm.render(scale);
        } else {
            model.bipedLeftArm.render(scale);
        }
    }

    @Desugar
    private record ModelKey(Item item, boolean slim) {

    }
}
