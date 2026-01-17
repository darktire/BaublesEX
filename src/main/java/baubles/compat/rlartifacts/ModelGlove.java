package baubles.compat.rlartifacts;

import artifacts.common.init.ModItems;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.registries.TypesData;
import baubles.client.model.ModelInherit;
import baubles.client.model.ModelManager;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class ModelGlove extends ModelInherit {
    private final Item item;
    private final ResourceLocation texture;
    private final ResourceLocation emissive;

    public static ModelGlove getInstance(ItemStack stack, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, renderPlayer, s -> new ModelGlove(s.getItem(), renderPlayer));
    }

    public ModelGlove(Item item, RenderPlayer renderPlayer) {
        super(renderPlayer.getMainModel(), null);
        this.item = item;
        this.texture = getResourceLocation(switchTex(item), renderPlayer.smallArms);
        this.emissive = getResourceLocation(switchLum(item), renderPlayer.smallArms);
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

    private EnumHandSide getHand(EntityLivingBase entity) {
        IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(entity);
        int k = baubles.indexOf(TypesData.Preset.RING, 0);
        int j = baubles.indexOf(this.item, 0);
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

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.texture;
    }

    @Override
    public ResourceLocation getEmissiveMap(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.emissive;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        renderGlove(this.getHand((EntityLivingBase) entity), scale);
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
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
