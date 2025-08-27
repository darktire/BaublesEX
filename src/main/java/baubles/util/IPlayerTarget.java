package baubles.util;

import net.minecraft.entity.EntityLivingBase;

public interface IPlayerTarget {
    void setTarget(EntityLivingBase target);
    EntityLivingBase getTarget();
}
