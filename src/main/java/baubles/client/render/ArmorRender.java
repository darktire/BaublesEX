package baubles.client.render;

import baubles.api.model.ModelBauble;
import baubles.api.render.IRenderBauble;
import baubles.client.model.ModelArmor;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmorRender implements IRenderBauble {

    private final ModelArmor model;

    public ArmorRender(ItemArmor armor, String tex) {
        this.model = new ModelArmor(armor, new ItemStack(armor));
        if (tex != null) this.model.setTexture(tex);
    }

    @Override
    public ModelBauble getModel(ItemStack stack, EntityLivingBase entity, RenderPlayer renderPlayer) {
        return this.model;
    }
}
