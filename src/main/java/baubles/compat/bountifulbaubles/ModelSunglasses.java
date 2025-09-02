package baubles.compat.bountifulbaubles;

import baubles.api.model.ModelBauble;
import cursedflames.bountifulbaubles.item.ModItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelSunglasses extends ModelBauble {
    private static ModelSunglasses instance;

    public ModelSunglasses() {
        super(ModItems.trinketMagicLenses, false);
        this.model = Resources.SUNGLASSES;
    }

    public static ModelSunglasses instance() {
        if (instance == null) instance = new ModelSunglasses();
        return instance;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        ((ModelBiped) this.model).bipedHead.render(scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ((ModelBiped) this.model).bipedHead.render(scale);
    }
}
