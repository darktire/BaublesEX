package baubles.lib.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.stream.Collectors;

public class ItemQuery {
    public static final ItemQuery EMPTY = new ItemQuery(ItemKey.of(ItemStack.EMPTY), NBTMatcher.ANY);
    private final ItemKey key;
    private final NBTMatcher matcher;

    private ItemQuery(ItemKey key, NBTMatcher matcher) {
        this.key = key;
        this.matcher = matcher;
    }

    public static ItemQuery of(ItemKey key) {
        return new ItemQuery(key, NBTMatcher.ANY);
    }

    public static ItemQuery of(ItemKey key, NBTMatcher matcher) {
        return new ItemQuery(key, matcher);
    }

    public static ItemQuery of(ItemStack stack) {
        return new ItemQuery(ItemKey.of(stack), stack.hasTagCompound() ? NBTMatcher.contains(stack.getTagCompound()) : NBTMatcher.ANY);
    }

    public static ItemQuery of(Item item) {
        return new ItemQuery(ItemKey.wildcard(item), NBTMatcher.ANY);
    }

    public ItemKey key() { return key; }
    public ItemKey wildcard() { return ItemKey.wildcard(key.getItem()); }
    public NBTMatcher matcher() { return matcher; }

    private ItemStack refCache;
    public ItemStack ref() {
        if (refCache == null) {
            refCache = new ItemStack(key.getItem(), 1, key.getMeta());
            refCache.setTagCompound(matcher.tag);
        }
        return refCache;
    }

    public static List<ItemQuery> ofOreDict(String oreName) {
        return OreDictionary.getOres(oreName, false).stream()
                .map(ItemQuery::of)
                .collect(Collectors.toList());
    }

    public static ItemQuery parse(String s) {
        String keyPart;
        NBTTagCompound tag = null;

        int nbtStart = s.indexOf(":{");
        if (nbtStart != -1) {
            keyPart = s.substring(0, nbtStart);
            String nbtPart = s.substring(nbtStart + 1);
            try {
                tag = JsonToNBT.getTagFromJson(nbtPart);
            } catch (NBTException e) {
                throw new IllegalArgumentException("Invalid NBT in: <" + s + ">", e);
            }
        } else {
            keyPart = s;
        }

        ItemKey key = ItemKey.parse(keyPart);
        return tag != null ? ItemQuery.of(key, NBTMatcher.contains(tag)) : ItemQuery.of(key);
    }

    @Override
    public String toString() {
        String tag = matcher.toString();
        return key + (tag.isEmpty() ? "" : ":{" + tag + "}");
    }
}