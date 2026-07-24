package baubles.common.module;

import com.google.common.base.Objects;
import com.google.common.base.Suppliers;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

import java.util.function.Supplier;

public class ModulePotion extends AbstractModule {
    protected final Potion potion;
    protected int perLevel;

    public ModulePotion(Potion potion, int perLevel, int limit) {
        this.max = limit - 1;
        this.perLevel = perLevel;
        this.potion = potion;
    }

    public static ModulePotion of(String name, int perLevel, int limit) {
        return new NameBased(name, perLevel, limit);
    }

    public void updateStatus(EntityLivingBase entity, int level) {
        Potion potion = getPotion();
        if (potion == null) return;

        level = getAmountIn(level) - 1;

        if (this.max != -1 && level > this.max) level = this.max;
        if (level == -1) {
            PotionEffect currentEffect = entity.getActivePotionEffect(potion);
            if (this.max == -1 || currentEffect != null && currentEffect.getAmplifier() <= this.max) {
                entity.removePotionEffect(potion);
            }
        }
        else {
            entity.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, level, true, true));
        }
    }

    protected Potion getPotion() {
        return this.potion;
    }

    protected int getAmountIn(int level) {
        return this.perLevel * level;
    }

    @Override
    public String getDescription() {
        return TextFormatting.BLUE + " " + I18n.format("info.baubles.module.potion", String.format("%+d", this.perLevel), I18n.format(getTranslateKey()));
    }

    protected String getTranslateKey() {
        return this.potion.getName();
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

    private static class NameBased extends ModulePotion {
        private final String name;
        private final Supplier<Potion> supplier;

        public NameBased(String name, int perLevel, int limit) {
            super(null, perLevel, limit);
            this.name = name;
            this.supplier = Suppliers.memoize(() -> Potion.getPotionFromResourceLocation(name));
        }

        @Override
        protected Potion getPotion() {
            return this.supplier.get();
        }

        @Override
        protected String getTranslateKey() {
            Potion potion = getPotion();
            return potion == null ? this.name : potion.getName();
        }
    }
}
