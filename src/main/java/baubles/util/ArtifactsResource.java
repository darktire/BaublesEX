package baubles.util;

import artifacts.Artifacts;
import artifacts.client.model.*;
import artifacts.common.init.ModItems;
import artifacts.common.util.RenderHelper;
import baubles.api.BaublesApi;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ArtifactsResource {
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

    public static final ResourceLocation FERAL_CLAWS_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/feral_claws_normal.png");
    public static final ResourceLocation POWER_GLOVE_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/power_glove_normal.png");
    public static final ResourceLocation MECHANICAL_GLOVE_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/mechanical_glove_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/fire_gauntlet_normal.png");
    public static final ResourceLocation FIRE_GAUNTLET_OVERLAY_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/fire_gauntlet_overlay_normal.png");
    public static final ResourceLocation POCKET_PISTON_TEXTURES = new ResourceLocation(Artifacts.MODID, "textures/entity/layer/pocket_piston_normal.png");

    public static final ModelBase AMULET_MODEL = new ModelAmulet();
    public static final ModelBase PANIC_MODEL = new ModelPanicNecklace();
    public static final ModelBase ULTIMATE_MODEL = new ModelUltimatePendant();

    public static final ModelBase BOTTLE_MODEL = new ModelBottledCloud();
    public static final ModelBase ANTIDOTE_MODEL = new ModelAntidoteVessel();
    public static final ModelBase BUBBLE_MODEL = new ModelBubbleWrap();
    public static final ModelBase SKULL_MODEL = new ModelObsidianSkull();

    public static final ModelCloak CLOAK_MODEL_UP = new ModelCloak() {
        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            renderHood(entity, f, f1, f2, f3, f4, f5, renderHood);
        }
    };
    public static final ModelCloak CLOAK_MODEL_DOWN = new ModelCloak() {
        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            renderCloak(entity, f, f1, f2, f3, f4, f5, renderHood);
        }
    };

    public static final ModelBase HAT_MODEL = new ModelDrinkingHat();

    public static final ModelNightVisionGoggles GOGGLES = new ModelNightVisionGoggles();

    public static final ModelSnorkel SNORKEL = new ModelSnorkel();

    private static boolean renderHood = true;
    public static void updateHoodState(EntityPlayer player, ItemStack stack) {
        if (stack.getItem() == ModItems.STAR_CLOAK) {
            renderHood = RenderHelper.shouldRenderInSlot(player, EntityEquipmentSlot.HEAD)
                    && (BaublesApi.isBaubleEquipped((EntityLivingBase) player, ModItems.DRINKING_HAT) == -1);
        }
    }

}
