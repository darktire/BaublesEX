package baubles.compat.artifacts;

import artifacts.common.init.ModItems;
import baubles.api.model.ModelBauble;
import baubles.client.ClientProxy;
import net.minecraft.client.model.ModelPlayer;
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
    private final ModelPlayer modelPlayer;
    private final ResourceLocation texture;
    private final ResourceLocation emissive;
    private EnumHandSide hand = initHand();

    public ModelGlove(Item item, boolean slim) {
        super(item, slim, null);
        this.texture = getResourceLocation(switchTex(item), slim);
        this.emissive = getResourceLocation(switchLum(item), slim);
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
        Map.Entry<Item, Boolean> pair = new AbstractMap.SimpleEntry<>(item, slim);
        ModelGlove model = instances.get(pair);
        if (model == null) {
            model = new ModelGlove(item, slim);
            instances.put(pair, model);
        }
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
        if (item == ModItems.POWER_GLOVE) return Resource.POWER_GLOVE_TEXTURE;
        else if(item == ModItems.FERAL_CLAWS) return Resource.FERAL_CLAWS_TEXTURE;
        else if(item == ModItems.MECHANICAL_GLOVE) return Resource.MECHANICAL_GLOVE_TEXTURE;
        else if(item == ModItems.FIRE_GAUNTLET) return Resource.FIRE_GAUNTLET_TEXTURE;
        else if(item == ModItems.POCKET_PISTON) return Resource.POCKET_PISTON_TEXTURE;
        return null;
    }

    private static ResourceLocation switchLum(Item item) {
        if(item == ModItems.FIRE_GAUNTLET || item == ModItems.MAGMA_STONE) return Resource.FIRE_GAUNTLET_OVERLAY_TEXTURE;
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
        this.switchSide();
        this.modelPlayer.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.callback();
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean needLocating() {
        return false;
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
        modelPlayer.bipedHead.showModel = false;
        modelPlayer.bipedHeadwear.showModel = false;
        modelPlayer.bipedBody.showModel = false;
        modelPlayer.bipedBodyWear.showModel = false;
        modelPlayer.bipedRightLeg.showModel = false;
        modelPlayer.bipedRightLegwear.showModel = false;
        modelPlayer.bipedLeftLeg.showModel = false;
        modelPlayer.bipedLeftLegwear.showModel = false;
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
