package baubles.util;

import net.minecraft.entity.EntityLivingBase;

public interface IHelper {
    void setTarget(EntityLivingBase target);
    EntityLivingBase getTarget();

    void setFlag(Boolean flag);
    boolean getFlag();
}
