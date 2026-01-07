package baubles.compat.rlartifacts;

import artifacts.Artifacts;
import artifacts.client.model.*;
import artifacts.common.init.ModItems;
import baubles.api.model.ModelBauble;
import baubles.client.model.ModelInherit;
import baubles.compat.CommonRcs;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Resources extends CommonRcs {

    public static final ResourceLocation SHOCK_TEXTURE = getLoc("shock_pendant.png");
    public static final ResourceLocation FLAME_TEXTURE = getLoc("flame_pendant.png");
    public static final ResourceLocation THORN_TEXTURE = getLoc("thorn_pendant.png");
    public static final ResourceLocation SACRIFICIAL_TEXTURE = getLoc("sacrificial_amulet.png");

    public static final ResourceLocation HAT_TEXTURE = getLoc("drinking_hat.png");
    public static final ResourceLocation HAT_SPECIAL_TEXTURE = getLoc("drinking_hat_special.png");

    public static final ResourceLocation FERAL_CLAWS_TEXTURE = getLoc("feral_claws_normal.png");
    public static final ResourceLocation POWER_GLOVE_TEXTURE = getLoc("power_glove_normal.png");
    public static final ResourceLocation MECHANICAL_GLOVE_TEXTURE = getLoc("mechanical_glove_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_TEXTURE = getLoc("fire_gauntlet_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_OVERLAY_TEXTURE = getLoc("fire_gauntlet_overlay_normal.png");
    public static final ResourceLocation POCKET_PISTON_TEXTURE = getLoc("pocket_piston_normal.png");

    static ResourceLocation getLoc(String path) {
        return getLoc(Artifacts.MODID, ENTITY_LAYER, path);
    }

    public static final ModelBauble AMULET_MODEL = new ModelArtifacts(new ModelAmulet(), null) {
        @Override
        public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
            Item item = stack.getItem();
            if (item == ModItems.SHOCK_PENDANT) return Resources.SHOCK_TEXTURE;
            else if(item == ModItems.FLAME_PENDANT) return Resources.FLAME_TEXTURE;
            else if(item == ModItems.THORN_PENDANT) return Resources.THORN_TEXTURE;
            else if(item == ModItems.SACRIFICIAL_AMULET) return Resources.SACRIFICIAL_TEXTURE;
            return null;
        }
    };
    public static final ModelBauble PANIC_MODEL = new ModelArtifacts(new ModelPanicNecklace(), getLoc("panic_necklace.png"));
    public static final ModelBauble ULTIMATE_MODEL = new ModelArtifacts(new ModelUltimatePendant(), getLoc("ultimate_pendant.png"));

    public static final ModelBauble BOTTLED_CLOUD = new ModelBottle(getLoc("bottled_cloud.png"));
    public static final ModelBauble BOTTLED_FART = new ModelBottle(getLoc("bottled_fart.png"));
    public static final ModelBauble ANTIDOTE_MODEL = new ModelArtifacts(new ModelAntidoteVessel(), getLoc("antidote_vessel.png"));
    public static final ModelBauble BUBBLE_MODEL = new ModelInherit(new ModelBubbleWrap(), getLoc("bubble_wrap.png")) {
        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ((ModelBubbleWrap) this.model).belt.render(scale);
        }
    };
    public static final ModelBauble SKULL_MODEL = new ModelArtifacts(new ModelObsidianSkull(), getLoc("obsidian_skull.png"));

    public static final ModelBauble CLOAK_MODEL_UP = new ModelCloak(true);
    public static final ModelBauble CLOAK_MODEL_DOWN = new ModelCloak(false);

    public static final ModelBauble HAT_MODEL = new ModelHat();

    public static final ModelBauble GOGGLES = new ModelInherit(new ModelNightVisionGoggles() {
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
    }, getLoc("night_vision_goggles.png"));

    private static boolean renderFull = true;
    public static final ModelBauble SNORKEL = new ModelInherit(new ModelSnorkel() {
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
    }, getLoc("snorkel.png"));

}
