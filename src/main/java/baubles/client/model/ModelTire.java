package baubles.client.model;

import baubles.BaublesRegister;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;
import java.util.Map;

public class ModelTire extends ModelItemHelper {
    private static ModelTire instance;
    private final Map<EntityLivingBase, Integer> angle = new HashMap<>();
    private final Map<EntityLivingBase, Integer> angleStart = new HashMap<>();

    public ModelTire() {
        super(BaublesRegister.ModItems.tire);
    }

    public static ModelTire instance() {
        if (instance == null) {
            instance = new ModelTire();
        }
        return instance;
    }

    @Override
    public void render(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean flag) {
        this.updateAngle(entity);
        GlStateManager.translate(0, 0, 0.5);
        GlStateManager.rotate((this.angle.get(entity) + partialTicks) / 2, 0, 0, 1);
        GlStateManager.scale(3, 3, 1);
        this.renderItem();
    }

    private void updateAngle(EntityLivingBase entity) {
        if (this.angle.containsKey(entity)) {
            this.angle.compute(entity, (k, v) -> k.ticksExisted - this.angleStart.get(k));
        }
        else {
            this.angle.put(entity, 0);
            this.angleStart.put(entity, entity.ticksExisted);
        }
    }

    public void resetAngle(EntityLivingBase entity) {
        this.angle.put(entity, 0);
        this.angleStart.put(entity, entity.ticksExisted);
    }
}
