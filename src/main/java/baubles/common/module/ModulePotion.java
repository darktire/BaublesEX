package baubles.common.module;

import com.google.common.base.Objects;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class ModulePotion extends AbstractModule{
    protected final Potion potion;
    protected int perLevel;

    public ModulePotion(Potion potion, int perLevel, int limit) {
        this.max = limit - 1;
        this.perLevel = perLevel;
        this.potion = potion;
    }

    public void updateStatus(EntityLivingBase entity, int level) {
        level -= 1;

        if (level > this.max) level = this.max;
        if (level == -1) {
            PotionEffect currentEffect = entity.getActivePotionEffect(this.potion);
            if (currentEffect != null && currentEffect.getAmplifier() <= this.max) {
                entity.removePotionEffect(this.potion);
            }
        }
        else {
            entity.addPotionEffect(new PotionEffect(this.potion, Integer.MAX_VALUE, level, true, true));
        }
    }

    @Override
    public String getDescription() {
        return TextFormatting.BLUE + " " + I18n.format("property.module.potion", String.format("%+d", this.perLevel), I18n.format(this.potion.getName()));
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
