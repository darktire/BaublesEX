package baubles.common.module;

import com.google.common.base.Objects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ModulePotion extends AbstractModule{
    protected final Potion potion;

    public ModulePotion(Potion potion, int limit) {
        this.limit = limit;
        this.potion = potion;
    }

    public void updateStatus(EntityLivingBase entity, int level) {
        if (level > this.limit) level = this.limit;
        PotionEffect currentEffect = entity.getActivePotionEffect(this.potion);
        int currentLevel = currentEffect != null ? currentEffect.getAmplifier() : -1;
        if (currentLevel >= level - 1) return;
        entity.removeActivePotionEffect(this.potion);
        if (level != -1 && !entity.world.isRemote) {
            entity.addPotionEffect(new PotionEffect(this.potion, Integer.MAX_VALUE, level, true, true));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.potion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof ModulePotion) {
            ModulePotion that = (ModulePotion) obj;

            if (this.potion != that.potion) return false;
            return this.id.equals(that.id);
        }
        return false;
    }
}
