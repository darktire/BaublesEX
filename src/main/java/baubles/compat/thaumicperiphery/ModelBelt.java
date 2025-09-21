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
import thaumcraft.api.items.ItemsTC;

import java.util.HashMap;
import java.util.Map;

public class ModelBelt extends ModelBauble {
    private static final Map<Item, ModelBelt> instances = new HashMap<>();
    private final ModelBiped model1;
    private final ModelRenderer model2;

    private ModelBelt(Item item) {
        super(item, false);
        this.model1 = new ModelBiped();
        this.model2 = new ModelRenderer(this.model1, 0, 11);
        this.model2.addBox(1.25F, 6.5F, -3.0F, 3, 3, 1);
    }

    public static ModelBelt instance(Item item) {
        return instances.putIfAbsent(item, new ModelBelt(item));
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        GlStateManager.translate(0.0F, 0.2F, 0.0F);
        float s = 1.05F;
        GlStateManager.scale(s, s, s);
        this.model1.bipedBody.render(scale);
        if (this.item == ItemsTC.focusPouch) this.model2.render(scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.model1.bipedBody.render(scale);
        if (this.item == ItemsTC.focusPouch) this.model2.render(scale);
    }
}
