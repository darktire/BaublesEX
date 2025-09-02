package baubles.compat.artifacts;

import artifacts.common.init.ModItems;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesModifiable;
import baubles.api.model.ModelBauble;
import baubles.api.registries.TypesData;
import baubles.client.ClientProxy;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModelGlove extends ModelBauble {
    private static final Map<Map.Entry<Item, Boolean>, ModelGlove> instances = new HashMap<>();
    private final ResourceLocation texture;
    private final ResourceLocation emissive;
    private static final BaubleTypeEx RING = TypesData.Preset.RING;

    public ModelGlove(Item item, boolean slim) {
        super(item, slim);
        this.texture = getResourceLocation(switchTex(item), slim);
        this.emissive = getResourceLocation(switchLum(item), slim);
//        this.model = new ModelPlayer(1, slim);
        this.model = slim ? ClientProxy.SLIM_LAYER.getModelPlayer() : ClientProxy.NORMAL_LAYER.getModelPlayer();
    }

    private static ResourceLocation getResourceLocation(ResourceLocation texture, boolean slim) {
        if (slim && texture != null) {
            String resourceName = texture.toString().replace("normal", "slim");
            return new ResourceLocation(resourceName);
        }
        else {
            return texture;
        }
    }

    public static ModelGlove instance(Item item, boolean slim) {
        Map.Entry<Item, Boolean> pair = new AbstractMap.SimpleEntry<>(item, slim);
        ModelGlove model = instances.get(pair);
        if (model == null) {
            model = new ModelGlove(item, slim);
            instances.put(pair, model);
        }
        return model;
    }

    private EnumHandSide getHand(EntityLivingBase entity) {
        IBaublesModifiable baubles = BaublesApi.getBaublesHandler(entity);
        int j = 0, k = 0;
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (baubles.getTypeInSlot(i) == RING) {
                j = i;
                break;
            }
        }
        for (int i = 0; i < baubles.getSlots(); i++) {
            if (baubles.getStackInSlot(i).getItem() == this.item) {
                k = i;
                break;
            }
        }
        return ((k - j) & 1) == 0 ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
    }

    private static ResourceLocation switchTex(Item item) {
        if (item == ModItems.POWER_GLOVE) return Resources.POWER_GLOVE_TEXTURE;
        else if(item == ModItems.FERAL_CLAWS) return Resources.FERAL_CLAWS_TEXTURE;
        else if(item == ModItems.MECHANICAL_GLOVE) return Resources.MECHANICAL_GLOVE_TEXTURE;
        else if(item == ModItems.FIRE_GAUNTLET) return Resources.FIRE_GAUNTLET_TEXTURE;
        else if(item == ModItems.POCKET_PISTON) return Resources.POCKET_PISTON_TEXTURE;
        return null;
    }

    private static ResourceLocation switchLum(Item item) {
        if(item == ModItems.FIRE_GAUNTLET || item == ModItems.MAGMA_STONE) return Resources.FIRE_GAUNTLET_OVERLAY_TEXTURE;
        return null;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public ResourceLocation getEmissive() {
        return this.emissive;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        renderGlove(this.getHand((EntityLivingBase) entity), scale);
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        if (entity.isSneaking() && flag) GlStateManager.translate(0, 0.2F, 0);
        renderGlove(this.getHand(entity), scale);
    }

    private void renderGlove(EnumHandSide hand, float scale) {
        if (hand == EnumHandSide.LEFT) {
            ((ModelPlayer) this.model).bipedLeftArm.render(scale);
        }
        else {
            ((ModelPlayer) this.model).bipedRightArm.render(scale);
        }
    }

    @Override
    public boolean needLocating() {
        return false;
    }
}
