package baubles.client.model;

import baubles.api.model.ModelBauble;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.main.ModConfig;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

public class ModelArmor extends ModelBauble.NoTex {

    protected ModelBiped model;
    protected final ItemArmor armor;
    protected final ItemStack armorStack;
    protected boolean unready = true;
    protected ResourceLocation texture;

    public ModelArmor(ItemArmor armor, ItemStack stack) {
        this.armor = armor;
        this.armorStack = stack;
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        initModel(entity);

        this.model.setModelAttributes(renderPlayer.getMainModel());
        this.model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);

        if (this.texture != null) {
            renderPlayer.bindTexture(this.texture);
        } else if (armor.hasOverlay(armorStack)) {
            int color = armor.getColor(armorStack);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >>  8 & 255) / 255.0F;
            float b = (float)(color       & 255) / 255.0F;
            GlStateManager.color(r, g, b, 1.0F);
            renderPlayer.bindTexture(getArmorResource(entity, null));
            this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            renderPlayer.bindTexture(getArmorResource(entity, "overlay"));
        } else {
            renderPlayer.bindTexture(getArmorResource(entity, null));
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    protected void initModel(EntityLivingBase entity) {
        if (this.unready) {
            EntityEquipmentSlot slot = armor.armorType;
            this.model = (slot == EntityEquipmentSlot.LEGS) ? new ModelBiped(0.5F) : new ModelBiped(1.0F);
            this.model = ForgeHooksClient.getArmorModel(entity, armorStack, slot, this.model);
            setModelSlotVisible(this.model, slot);
            this.unready = false;
        }
    }

    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slot) {
        model.setVisible(false);

        switch (slot) {
            case HEAD:
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                break;
            case CHEST:
                model.bipedBody.showModel = true;
                model.bipedLeftArm.showModel = true;
                model.bipedRightArm.showModel = true;
                break;
            case LEGS:
                model.bipedBody.showModel = true;
                model.bipedLeftLeg.showModel = true;
                model.bipedRightLeg.showModel = true;
                break;
            case FEET:
                model.bipedLeftLeg.showModel = true;
                model.bipedRightLeg.showModel = true;
                break;
            default:
                break;
        }
    }

    private ResourceLocation getArmorResource(Entity entity, String type) {
        String texture = this.armor.getArmorMaterial().getName();
        String domain  = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain  = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String path = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (this.armor.armorType == EntityEquipmentSlot.LEGS ? 2 : 1), type == null ? "" : "_" + type);

        return new ResourceLocation(ForgeHooksClient.getArmorTexture(entity, this.armorStack, path, this.armor.armorType, type));
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        LayerArmorBase.renderEnchantedGlint(renderPlayer, entity, this.model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    public void setTexture(String texture) {
        this.texture = new ResourceLocation(texture);
//        this.overlay = new ResourceLocation(texture + "_overlay");
    }

    public static class WithBends extends ModelArmor {

        public WithBends(ItemArmor armor, ItemStack stack) {
            super(armor, stack);
        }

        @Override
        protected void initModel(EntityLivingBase entity) {
            if (this.unready) {
                EntityEquipmentSlot slot = armor.armorType;
                this.model = (slot == EntityEquipmentSlot.LEGS) ? new ModelBiped(0.5F) : new ModelBiped(1.0F);
                this.model = ForgeHooksClient.getArmorModel(entity, armorStack, slot, this.model);
                EntityData<?> entityData = EntityDatabase.instance.get(entity);
                boolean shouldBeMutated = !ModConfig.shouldKeepArmorAsVanilla(armor) && entityData instanceof BipedEntityData;
                this.model = ArmorModelFactory.getArmorModel(model, shouldBeMutated);
                setModelSlotVisible(this.model, slot);
                this.unready = false;
            }
        }
    }
}
