package baubles.lib.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.stream.Collectors;

public class ItemKey {
    private final Item item;
    private final int meta;

    protected ItemKey(Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }

    public static ItemKey of(Item item) {
        return new ItemKey(item, item.getHasSubtypes() ? 0 : OreDictionary.WILDCARD_VALUE);
    }

    public static ItemKey of(Item item, int meta) {
        return new ItemKey(item, meta);
    }

    public static ItemKey of(ItemStack stack) {
        Item item = stack.getItem();
        int meta = item.getHasSubtypes() ? stack.getMetadata() : OreDictionary.WILDCARD_VALUE;
        return new ItemKey(item, meta);
    }

    public static ItemKey wildcard(Item item) {
        return new ItemKey(item, OreDictionary.WILDCARD_VALUE);
    }

    public boolean matches(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() != this.item) return false;
        return this.isWildcard() || this.meta == stack.getMetadata();
    }

    public boolean matches(ItemKey key) {
        if (key.item != this.item) return false;
        return this.isWildcard() || key.isWildcard() || this.meta == key.meta;
    }

    public boolean matches(Item item, int meta) {
        if (item != this.item) return false;
        return this.isWildcard() || this.meta == meta;
    }

    public Item getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }

    public boolean isWildcard() { return meta == OreDictionary.WILDCARD_VALUE; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemKey)) return false;
        ItemKey other = (ItemKey) o;
        return this.meta == other.meta && this.item == other.item;
    }

    @Override
    public int hashCode() {
        return 31 * System.identityHashCode(item) + meta;
    }

    @Override
    public String toString() {
        return item.getRegistryName() + (item.getHasSubtypes() ? (isWildcard() ? ":*" : ":" + meta) : "");
    }

    public static List<ItemKey> ofOreDict(String oreName) {
        List<ItemStack> ores = OreDictionary.getOres(oreName, false);
        return ores.stream().map(ItemKey::of).collect(Collectors.toList());
    }

    public static ItemKey parse(String s) {
        Item item = null;
        int meta = OreDictionary.WILDCARD_VALUE;
        String[] seg = s.split(":");
        switch (seg.length) {
            case 1:
                item = Item.getByNameOrId(seg[0]);
                break;
            case 2:
                if (seg[1].matches("\\d+")) {
                    item = Item.getByNameOrId(seg[0]);
                    meta = Integer.parseInt(seg[1]);
                } else if (seg[1].equals("*")) {
                    item = Item.getByNameOrId(seg[0]);
                } else {
                    item = Item.getByNameOrId(s);
                }
                break;
            default:
                if (seg[2].matches("\\d+")) {
                    item = Item.getByNameOrId(seg[0] + ":" + seg[1]);
                    meta = Integer.parseInt(seg[2]);
                } else if (seg[2].equals("*")) {
                    item = Item.getByNameOrId(seg[0] + ":" + seg[1]);
                }
        }

        if (item == null) {
            throw new IllegalArgumentException("Unknown item: <" + s + ">");
        }

        return new ItemKey(item, meta);
    }
}
