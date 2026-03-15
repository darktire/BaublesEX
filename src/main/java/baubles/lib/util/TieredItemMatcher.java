package baubles.lib.util;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TieredItemMatcher<V> {

    private final Map<ItemKey, List<Tier<V>>> map = new HashMap<>();
    private TieredItemMatcher<V> backup;

    public void register(ItemQuery query, V value) {
        List<Tier<V>> tiers = map.computeIfAbsent(query.wildcard(), k -> new ArrayList<>());
        insertSorted(tiers, new Tier<>(query.key(), query.matcher(), value));
    }

    public V computeIfAbsent(ItemQuery query, Supplier<V> supplier) {
        List<Tier<V>> tiers = map.computeIfAbsent(query.wildcard(), k -> new ArrayList<>());
        for (Tier<V> tier : tiers) {
            if (tier.key.equals(query.key()) && tier.matcher.equals(query.matcher())) {
                return tier.value;
            }
        }
        V value = supplier.get();
        insertSorted(tiers, new Tier<>(query.key(), query.matcher(), value));
        return value;
    }

    public V match(ItemStack stack) {
        if (stack.isEmpty()) return null;
        List<Tier<V>> tiers = map.get(ItemKey.wildcard(stack.getItem()));
        if (tiers == null) return null;
        NBTTagCompound nbt = stack.getTagCompound();
        for (Tier<V> tier : tiers) {
            if (tier.key.matches(stack) && tier.matcher.test(nbt)) return tier.value;
        }
        return null;
    }

    public boolean contains(ItemQuery query) {
        List<Tier<V>> tiers = map.get(query.wildcard());
        if (tiers == null) return false;
        for (Tier<V> tier : tiers) {
            if (tier.key.matches(query.key()) && tier.matcher.test(query.matcher().tag)) return true;
        }
        return false;
    }

    private void insertSorted(List<Tier<V>> tiers, Tier<V> tier) {
        int priority = tier.priority();
        int lo = 0, hi = tiers.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (tiers.get(mid).priority() >= priority) lo = mid + 1;
            else hi = mid;
        }
        tiers.add(lo, tier);
    }

    public List<ItemQuery> getKeys() {
        return map.values().stream()
                .flatMap(List::stream)
                .map(t -> ItemQuery.of(t.key, t.matcher))
                .collect(Collectors.toList());
    }
    public void clear() {
        map.clear();
    }

    public TieredItemMatcher<V> copy() {
        TieredItemMatcher<V> copy = new TieredItemMatcher<>();
        map.forEach((key, tiers) -> copy.map.put(key, new ArrayList<>(tiers)));
        return copy;
    }

    public void backup()  {
        this.backup = copy();
    }
    public void restore() {
        map.clear();
        if (backup != null) backup.map.forEach((key, tiers) -> map.put(key, new ArrayList<>(tiers)));
    }

    @Desugar
    private record Tier<V>(ItemKey key, NBTMatcher matcher, V value) {
        int priority() {
                return (key.isWildcard() ? 0 : 1) + matcher.specificity();
            }
    }
}