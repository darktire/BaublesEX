package baubles.lib.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class NBTMatcher implements Predicate<NBTTagCompound> {

    public enum Mode { ANY, CONTAINS, EXACT }

    public static final NBTMatcher ANY = new NBTMatcher(Mode.ANY, null) {
        @Override
        public boolean test(NBTTagCompound nbt) { return true; }
        @Override
        public int specificity() { return 0; }
    };

    public static NBTMatcher contains(NBTTagCompound required) {
        return new NBTMatcher(Mode.CONTAINS, required.copy()) {
            @Override
            public boolean test(NBTTagCompound nbt) {
                return nbt != null && nbtContainsAll(nbt, tag);
            }
        };
    }

    public static NBTMatcher exact(NBTTagCompound required) {
        return new NBTMatcher(Mode.EXACT, required.copy()) {
            @Override
            public boolean test(NBTTagCompound nbt) { return tag.equals(nbt); }
            @Override
            public int specificity() { return super.specificity() + 1; }
        };
    }

    private final Mode mode;
    protected final NBTTagCompound tag;

    private NBTMatcher(Mode mode, NBTTagCompound tag) {
        this.mode = mode;
        this.tag  = tag;
    }

    public Mode getMode() { return mode; }

    public int specificity() {
        return nbtSpecificity(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NBTMatcher)) return false;
        NBTMatcher other = (NBTMatcher) o;
        return this.mode == other.mode && Objects.equals(this.tag, other.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, tag);
    }

    @Override
    public String toString() {
        return mode == Mode.ANY ? "" : tag.toString();
    }

    private static int nbtSpecificity(NBTTagCompound tag) {
        int count = 0;
        for (String key : tag.getKeySet()) {
            count++;
            NBTBase val = tag.getTag(key);
            if (val instanceof NBTTagCompound) count += nbtSpecificity((NBTTagCompound) val);
        }
        return count;
    }

    private static boolean nbtContainsAll(NBTTagCompound target, NBTTagCompound required) {
        for (String key : required.getKeySet()) {
            if (!target.hasKey(key)) return false;
            NBTBase reqVal = required.getTag(key);
            NBTBase tgtVal = target.getTag(key);
            if (reqVal.getId() != tgtVal.getId()) return false;
            if (reqVal instanceof NBTTagCompound) {
                if (!nbtContainsAll((NBTTagCompound) tgtVal, (NBTTagCompound) reqVal)) return false;
            } else {
                if (!reqVal.equals(tgtVal)) return false;
            }
        }
        return true;
    }
}