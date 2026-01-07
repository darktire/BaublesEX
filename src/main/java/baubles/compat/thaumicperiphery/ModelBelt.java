package baubles.compat.thaumicperiphery;

import baubles.api.model.ModelBauble;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.items.ItemsTC;

public class ModelBelt extends ModelBauble {
    private final ModelRenderer model;
    private final ResourceLocation res;

    public ModelBelt(ItemStack stack) {
        ModelBiped biped = new ModelBiped();
        Item item = stack.getItem();
        this.res = switchTex(item, stack.getMetadata());
        if (item == ItemsTC.focusPouch) {
            this.model = new ModelRenderer(biped, 0, 11);
            this.model.addBox(1.25F, 6.5F, -3.0F, 3, 3, 1);
        }
        else {
            this.model = biped.bipedBody;
        }
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
        float s = 1.05F;
        GlStateManager.scale(s, s, s);
        this.model.render(scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.model.render(scale);
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.res;
    }

    private static ResourceLocation switchTex(Item item, int meta) {
        if (item == ItemsTC.focusPouch) return Resources.FOCUS_POUCH;
        else if (item == ItemsTC.baubles) {
            if (meta == 2) return Resources.GIRDLE_MUNDANE;
            else if (meta == 6) return Resources.GIRDLE_FANCY;
        }
        return null;
    }
}
