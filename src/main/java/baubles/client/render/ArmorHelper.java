package baubles.client.render;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelArmor;
import baubles.client.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;


public class ArmorHelper implements IRenderBauble {
    public static ArmorHelper INSTANCE = new ArmorHelper();

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return ModelManager.getInstance(stack, renderPlayer, a -> new ModelArmor());
    }
}
