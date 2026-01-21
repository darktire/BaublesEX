package baubles.api.module;

import net.minecraft.entity.EntityLivingBase;

public interface IModule {
    void updateStatus(EntityLivingBase entity, int level);
}
