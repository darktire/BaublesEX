package baubles.compat.bountifulbaubles;

import baubles.client.model.ModelInherit;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ModelSunglasses extends ModelInherit {

    public ModelSunglasses() {
        super(new cursedflames.bountifulbaubles.client.model.ModelSunglasses(), Resources.getLoc("sunglasses_layer_1.png"));
    }

    @Override
    public void render(RenderPlayer renderPlayer, EntityLivingBase entity, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        ((ModelBiped) this.model).bipedHead.render(scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ((ModelBiped) this.model).bipedHead.render(scale);
    }
}
