package baubles.client.model;

import artifacts.common.init.ModItems;
import baubles.client.ClientProxy;
import baubles.util.ArtifactsResource;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModelGlove extends ModelBase {
    private static final Map<Map.Entry<Item, Boolean>, ModelGlove> instances = new HashMap<>();
    private final Item item;
    private final ModelPlayer modelPlayer;
    private final ResourceLocation texture;
    private final ResourceLocation luminous;
    private EnumHandSide hand = initHand();

    public ModelGlove(Item item, boolean slim) {
        this.item = item;
        this.texture = getResourceLocation(switchTex(item), slim);
        this.luminous = getResourceLocation(switchLum(item), slim);
        this.modelPlayer = slim ? ClientProxy.SLIM_LAYER.getModelPlayer() : ClientProxy.NORMAL_LAYER.getModelPlayer();
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
        for (Map.Entry<Map.Entry<Item, Boolean>, ModelGlove> entry : instances.entrySet()) {
            Map.Entry<Item, Boolean> entry1 = entry.getKey();
            if (entry1.getKey() == item && entry1.getValue() == slim) return entry.getValue();
        }
        ModelGlove model = new ModelGlove(item, slim);
        Map.Entry<Item, Boolean> pair = new AbstractMap.SimpleEntry<>(item, slim);
        instances.put(pair, model);
        return model;
    }

    public void setHand(EnumHandSide hand) {
        this.hand = hand;
    }

    private static EnumHandSide initHand() {
        int flag = (int) (Math.random() * 2);
        return flag == 0 ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    private static ResourceLocation switchTex(Item item) {
        if (item == ModItems.POWER_GLOVE) return ArtifactsResource.POWER_GLOVE_TEXTURES;
        else if(item == ModItems.FERAL_CLAWS) return ArtifactsResource.FERAL_CLAWS_TEXTURES;
        else if(item == ModItems.MECHANICAL_GLOVE) return ArtifactsResource.MECHANICAL_GLOVE_TEXTURES;
        else if(item == ModItems.FIRE_GAUNTLET) return ArtifactsResource.FIRE_GAUNTLET_TEXTURES;
        else if(item == ModItems.POCKET_PISTON) return ArtifactsResource.POCKET_PISTON_TEXTURES;
        return null;
    }

    private static ResourceLocation switchLum(Item item) {
        if(item == ModItems.FIRE_GAUNTLET || item == ModItems.MAGMA_STONE) return ArtifactsResource.FIRE_GAUNTLET_OVERLAY_TEXTURES;
        return null;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public ResourceLocation getLuminous() {
        return this.luminous;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        this.switchSide();
        this.modelPlayer.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.callback();
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
    }

    private void switchSide() {
        if(this.hand == EnumHandSide.LEFT) {
            modelPlayer.bipedLeftArm.showModel = true;
            modelPlayer.bipedLeftArmwear.showModel = true;
            modelPlayer.bipedRightArm.showModel = false;
            modelPlayer.bipedRightArmwear.showModel = false;
        }
        else {
            modelPlayer.bipedLeftArm.showModel = false;
            modelPlayer.bipedLeftArmwear.showModel = false;
            modelPlayer.bipedRightArm.showModel = true;
            modelPlayer.bipedRightArmwear.showModel = true;
        }
    }

    private void callback() {
        if(this.hand == EnumHandSide.LEFT) {
            modelPlayer.bipedLeftArmwear.showModel = false;
            modelPlayer.bipedLeftArm.showModel = false;
        }
        else {
            modelPlayer.bipedRightArmwear.showModel = false;
            modelPlayer.bipedRightArm.showModel = false;
        }
    }
}
