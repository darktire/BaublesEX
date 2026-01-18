package baubles.client.model;

import baubles.api.model.ModelBauble;
import baubles.util.IArmorInherit;
import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomBipedArmor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModelArmor extends ModelBauble {
    private static final Map<RenderLivingBase<?>, LayerArmorBase<?>> LAYERS = new HashMap<>();
    private static final Function<RenderLivingBase<?>, LayerArmorBase<?>> FUNCTION = ModelInherit.FLAG ? LayerCustomBipedArmor::new : LayerBipedArmor::new;

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        LayerArmorBase<?> layer = LAYERS.computeIfAbsent(renderPlayer, FUNCTION);
        ((IArmorInherit) layer).render(entity, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public void renderEnchantedGlint(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, ModelBauble model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

    }
}
