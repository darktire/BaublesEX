package baubles.mixin.early.vanilla;

import baubles.util.IArmorInherit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;

@Mixin(LayerArmorBase.class)
@Implements(@Interface(iface = IArmorInherit.class, prefix = "brs$"))
public abstract class MixinLayerArmorBase<T extends ModelBase> {

    @Shadow private float colorR;
    @Shadow private float colorG;
    @Shadow private float colorB;
    @Shadow private float alpha;
    @Shadow private boolean skipRenderGlint;
    @Shadow @Final private RenderLivingBase<?> renderer;

    @Shadow public abstract T getModelFromSlot(EntityEquipmentSlot slotIn);
    @Shadow(remap = false) protected abstract T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model);
    @Shadow protected abstract void setModelSlotVisible(T p_188359_1_, EntityEquipmentSlot slotIn);
    @Shadow protected abstract boolean isLegSlot(EntityEquipmentSlot slotIn);
    @Shadow(remap = false) public abstract ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type);

    @Unique
    public void brs$render(EntityLivingBase entityLivingBaseIn, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor itemarmor = (ItemArmor)stack.getItem();
            EntityEquipmentSlot slotIn = itemarmor.getEquipmentSlot();

            if (itemarmor.getEquipmentSlot() == slotIn) {
                T t = this.getModelFromSlot(slotIn);
                t = getArmorModelHook(entityLivingBaseIn, stack, slotIn, t);
                t.setModelAttributes(this.renderer.getMainModel());
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.setModelSlotVisible(t, slotIn);
                boolean flag = this.isLegSlot(slotIn);
                this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, stack, slotIn, null));

                {
                    if (itemarmor.hasOverlay(stack)) // Allow this for anything, not only cloth
                    {
                        int i = itemarmor.getColor(stack);
                        float f = (float)(i >> 16 & 255) / 255.0F;
                        float f1 = (float)(i >> 8 & 255) / 255.0F;
                        float f2 = (float)(i & 255) / 255.0F;
                        GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                        this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, stack, slotIn, "overlay"));
                    }
                    { // Non-colored
                        GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                        t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    } // Default
                    if (!this.skipRenderGlint && stack.hasEffect())
                    {
                        LayerArmorBase.renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                    }
                }
            }
        }
    }
}
