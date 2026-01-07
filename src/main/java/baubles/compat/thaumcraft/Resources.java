package baubles.compat.thaumcraft;

import baubles.api.model.ModelBauble;
import baubles.client.model.ModelInherit;
import baubles.compat.CommonRcs;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.client.lib.UtilsFX;

public class Resources extends CommonRcs {

    public static final ModelBauble GOGGLES = new ModelInherit(new ModelBiped(), getLoc("textures/entity/armor/goggles.png")) {
        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            boolean armor = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
            float s = armor ? 1.25F : 1.2F;
            GlStateManager.scale(s, s, s);
            GlStateManager.translate(0, 0.05, 0);
            ((ModelBiped) this.model).bipedHead.render(scale);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ((ModelBiped) this.model).bipedHead.render(scale);
        }
    };

    public static final ModelBauble CURIOUS_BAND = new ModelBauble() {
        private final ResourceLocation res = getLoc("textures/items/curiosity_band_worn.png");

        @Override
        public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
            boolean armor = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            float s = 0.5F;
            GlStateManager.scale(s, s, s);
            GlStateManager.translate(-0.5, 0.125, (armor ? 0 : 0.11999999731779099) - 0.5);
            UtilsFX.renderTextureIn3D(0.0f, 0.0f, 1.0f, 1.0f, 16, 26, 0.1f);
        }

        @Override
        public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            UtilsFX.renderTextureIn3D(0.0f, 0.0f, 1.0f, 1.0f, 16, 26, 0.1f);
        }

        @Override
        public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
            return res;
        }
    };



    static ResourceLocation getLoc(String path) {
        return getLoc(Thaumcraft.MODID, "", path);
    }
}
