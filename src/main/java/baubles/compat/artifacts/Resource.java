package baubles.compat.artifacts;

import artifacts.Artifacts;
import artifacts.client.model.*;
import baubles.api.model.ModelBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class Resource {
    public static final ResourceLocation SHOCK_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/shock_pendant.png");
    public static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/flame_pendant.png");
    public static final ResourceLocation THORN_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/thorn_pendant.png");
    public static final ResourceLocation PANIC_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/panic_necklace.png");
    public static final ResourceLocation ULTIMATE_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/ultimate_pendant.png");
    public static final ResourceLocation SACRIFICIAL_TEXTURE = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/sacrificial_amulet.png");

    public static final ResourceLocation BOTTLED_CLOUD = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/bottled_cloud.png");
    public static final ResourceLocation BOTTLED_FART = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/bottled_fart.png");
    public static final ResourceLocation ANTIDOTE_VESSEL = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/antidote_vessel.png");
    public static final ResourceLocation BUBBLE_WRAP = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/bubble_wrap.png");
    public static final ResourceLocation OBSIDIAN_SKULL = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/obsidian_skull.png");

    public static final ResourceLocation CLOAK_NORMAL = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/star_cloak.png");
    public static final ResourceLocation CLOAK_OVERLAY = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/star_cloak_overlay.png");

    public static final ResourceLocation HAT_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/drinking_hat.png");
    public static final ResourceLocation HAT_SPECIAL_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/drinking_hat_special.png");

    public static final ResourceLocation FERAL_CLAWS_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/feral_claws_normal.png");
    public static final ResourceLocation POWER_GLOVE_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/power_glove_normal.png");
    public static final ResourceLocation MECHANICAL_GLOVE_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/mechanical_glove_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/fire_gauntlet_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_OVERLAY_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/fire_gauntlet_overlay_normal.png");
    public static final ResourceLocation POCKET_PISTON_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/pocket_piston_normal.png");

    public static final ModelBauble AMULET_MODEL = new ModelBauble(new ModelAmulet());
    public static final ModelBauble PANIC_MODEL = new ModelBauble(new ModelPanicNecklace());
    public static final ModelBauble ULTIMATE_MODEL = new ModelBauble(new ModelUltimatePendant());

    public static final ModelBauble BOTTLE_MODEL = new ModelBauble(new ModelBottledCloud());
    public static final ModelBauble ANTIDOTE_MODEL = new ModelBauble(new ModelAntidoteVessel());
    public static final ModelBauble BUBBLE_MODEL = new ModelBauble(new ModelBubbleWrap());
    public static final ModelBauble SKULL_MODEL = new ModelBauble(new ModelObsidianSkull());

    public static final ModelBauble CLOAK_MODEL_UP = new ModelBauble(new ModelCloak() {
        @Override
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {
            this.renderHood(entity, 0, 0, 0, 0, 0, scale, renderHood);
        }
    });
    public static final ModelBauble CLOAK_MODEL_DOWN = new ModelBauble(new ModelCloak() {
        @Override
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {
            this.renderCloak(entity, 0, 0, 0, 0, 0, scale, renderHood);
        }
    });

    public static final ModelBauble HAT_MODEL = new ModelBauble(new ModelDrinkingHat() {
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entity instanceof EntityPlayer) {
                if (entity.getName().equals("wouterke")) {
                    this.hatShade.showModel = ((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
                } else {
                    this.hatShade.showModel = false;
                }
            }
            this.hat.render(scale);
        }
    });

    public static final ModelBauble GOGGLES = new ModelBauble(new ModelNightVisionGoggles());

    public static final ModelBauble SNORKEL = new ModelBauble(new ModelSnorkel());

    private static boolean renderHood = true;
    public static void setHoodState(boolean render) {
        renderHood = render;
    }
    public static boolean getHoodState() {
        return renderHood;
    }

}
