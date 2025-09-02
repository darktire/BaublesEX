package baubles.compat.artifacts;

import artifacts.Artifacts;
import artifacts.client.model.*;
import baubles.api.model.ModelBauble;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class Resources {
    private static final String ENTITY_LAYER = "textures/entity/layer/";
    
    
    public static final ResourceLocation SHOCK_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "shock_pendant.png");
    public static final ResourceLocation FLAME_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "flame_pendant.png");
    public static final ResourceLocation THORN_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "thorn_pendant.png");
    public static final ResourceLocation PANIC_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "panic_necklace.png");
    public static final ResourceLocation ULTIMATE_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "ultimate_pendant.png");
    public static final ResourceLocation SACRIFICIAL_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "sacrificial_amulet.png");

    public static final ResourceLocation BOTTLED_CLOUD = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "bottled_cloud.png");
    public static final ResourceLocation BOTTLED_FART = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "bottled_fart.png");
    public static final ResourceLocation ANTIDOTE_VESSEL = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "antidote_vessel.png");
    public static final ResourceLocation BUBBLE_WRAP = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "bubble_wrap.png");
    public static final ResourceLocation OBSIDIAN_SKULL = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "obsidian_skull.png");

    public static final ResourceLocation CLOAK_NORMAL = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "star_cloak.png");
    public static final ResourceLocation CLOAK_OVERLAY = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "star_cloak_overlay.png");

    public static final ResourceLocation HAT_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "drinking_hat.png");
    public static final ResourceLocation HAT_SPECIAL_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "drinking_hat_special.png");

    public static final ResourceLocation FERAL_CLAWS_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "feral_claws_normal.png");
    public static final ResourceLocation POWER_GLOVE_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "power_glove_normal.png");
    public static final ResourceLocation MECHANICAL_GLOVE_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "mechanical_glove_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "fire_gauntlet_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_OVERLAY_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "fire_gauntlet_overlay_normal.png");
    public static final ResourceLocation POCKET_PISTON_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "pocket_piston_normal.png");

    public static final ResourceLocation GOGGLES_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "night_vision_goggles.png");
    
    public static final ResourceLocation SNORKEL_TEXTURE = new ResourceLocation(Artifacts.MODID, ENTITY_LAYER + "snorkel.png");
    
    public static final ModelBauble AMULET_MODEL = new ModelArtifacts(new ModelAmulet());
    public static final ModelBauble PANIC_MODEL = new ModelArtifacts(new ModelPanicNecklace());
    public static final ModelBauble ULTIMATE_MODEL = new ModelArtifacts(new ModelUltimatePendant());

    public static final ModelBauble BOTTLE_MODEL = new ModelArtifacts(new ModelBottledCloud()) {
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            boolean hasPants = !((EntityPlayer)entity).getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.1666666F, 1.1666666F, 1.1666666F);
            GlStateManager.scale(1.0F, 1.0F, hasPants ? 1.2F : 1.1F);
            ((ModelBottledCloud) this.model).belt.render(scale);
            GlStateManager.scale(1.0F, 1.0F, hasPants ? 0.8333333F : 0.9090909F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 1.0F, -0.6666667F);
            GlStateManager.translate(0.2F, 0.0F, 0.0F);
            GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
            ((ModelBottledCloud) this.model).jar.render(scale);
            ((ModelBottledCloud) this.model).lid.render(scale);
            GlStateManager.popMatrix();
        }
    };
    public static final ModelBauble ANTIDOTE_MODEL = new ModelArtifacts(new ModelAntidoteVessel());
    public static final ModelBauble BUBBLE_MODEL = new ModelBauble(new ModelBubbleWrap()) {
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ((ModelBubbleWrap) this.model).belt.render(scale);
        }
    };
    public static final ModelBauble SKULL_MODEL = new ModelArtifacts(new ModelObsidianSkull());

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

    public static final ModelBauble GOGGLES = new ModelBauble(new ModelNightVisionGoggles() {
        @Override
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {
            this.headBand.showModel = true;
            this.goggleBase.showModel = true;
            this.eyeLeft.showModel = true;
            this.eyeRight.showModel = true;
            this.headBand.render(scale);
            this.goggleBase.showModel = false;
            this.eyeLeft.showModel = false;
            this.eyeRight.showModel = false;
            this.headBand.showModel = false;
            float lastLightmapX = OpenGlHelper.lastBrightnessX;
            float lastLightmapY = OpenGlHelper.lastBrightnessY;
            this.eyeLeftOverlay.showModel = true;
            this.eyeRightOverlay.showModel = true;
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            this.eyeLeftOverlay.render(scale);
            this.eyeRightOverlay.render(scale);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastLightmapX, lastLightmapY);
            GlStateManager.enableLighting();
            this.eyeLeftOverlay.showModel = false;
            this.eyeRightOverlay.showModel = false;
        }
    });

    private static boolean renderFull = true;
    public static final ModelBauble SNORKEL = new ModelBauble(new ModelSnorkel() {
        public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float scale) {
            this.snorkelMouthPiece.showModel = true;
            this.snorkelTubeThing.showModel = true;
            this.snorkelGoggles.showModel = renderFull;
            this.snorkelMouthPiece.render(scale);
            this.snorkelGoggles.showModel = false;
            this.snorkelMouthPiece.showModel = false;
            this.snorkelTubeThing.showModel = false;
            if (renderFull) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.3F);
                this.snorkelGogglesOverlay.showModel = true;
                this.snorkelMouthPiece.render(scale);
                this.snorkelGogglesOverlay.showModel = false;
                GlStateManager.disableBlend();
            }
        }
    });

    private static boolean renderHood = true;
    public static void setHoodState(boolean render) {
        renderHood = render;
    }
    public static boolean getHoodState() {
        return renderHood;
    }
}
