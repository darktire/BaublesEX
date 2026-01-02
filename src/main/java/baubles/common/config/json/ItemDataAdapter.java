package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.BaublesWrapper;
import baubles.api.IBaubleKey;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import baubles.util.ItemParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ItemDataAdapter extends CustomAdapter<List<IBaubleKey>> {

    @Override
    public void write(JsonWriter out, List<IBaubleKey> value) throws IOException {
        Map<GroupKey, GroupResult> map = sort(value);
        out.beginObject();
        int i = 0;
        for (Map.Entry<GroupKey, GroupResult> entry : map.entrySet()) {
            out.name("block" + i++);
            out.beginObject();
            GroupKey key = entry.getKey();
            GroupResult result = entry.getValue();
            out.name("content");
            out.beginArray();
            for (String inner : result.names) {
                out.value(inner);
            }
            out.endArray();
            if (key.removed) {
                out.name("addition").value("remove");
            }
            else {
                out.name("types");
                out.beginArray();
                for (BaubleTypeEx type : key.types) {
                    out.value(String.valueOf(type.getRegistryName()));
                }
                out.endArray();
            }
            out.endObject();
        }
        out.endObject();
    }

    private static Map<GroupKey, GroupResult> sort(List<IBaubleKey> value) {
        List<ItemStack> stacks = value.stream().map(IBaubleKey::ref).collect(Collectors.toList());
        return stacks.stream().collect(
                Collectors.groupingBy(
                        GroupKey::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                s -> {
                                    GroupResult result = new GroupResult();
                                    s.forEach(result::add);
                                    return result;
                                })));
    }

    @Override
    public List<IBaubleKey> read(JsonReader in) throws IOException {
        parseObject(in, this::parseItem);
        return null;
    }

    private void parseItem(JsonReader in, String name) throws IOException {
        Temp temp = new Temp();
        List<String> content = new ArrayList<>();
        content.add(name);
        parseObject(in, (reader, key) -> {
            switch (key) {
                case "content":
                    content.clear();
                    parseArray(in, content::add);
                    break;
                case "types":
                    List<BaubleTypeEx> types = new ArrayList<>();
                    parseArray(in, typeName -> {
                        BaubleTypeEx type = TypesData.getTypeByName(typeName);
                        if (type != null) types.add(type);
                    });
                    temp.types = types;
                    break;
                case "addition":
                    parseArray(in, s -> {
                        switch (s) {
                            case "remove": temp.remove = true; break;
                        }
                    });
                    break;
                default:
                    in.skipValue();
                    break;
            }
        });
        temp.content = content;
        temp.apply();
    }

    static final class Temp {
        List<String> content;
        List<BaubleTypeEx> types;
        boolean remove;

        void apply() {
            for (IBaubleKey key : getContent(this.content)) {
                ItemsData.registerBauble(key, types);
                if (this.remove) BaublesWrapper.CSTMap.instance().update(key, attribute -> attribute.remove(true));
            }
        }

        static Set<IBaubleKey> getContent(List<String> names) {
            Set<IBaubleKey> set = new HashSet<>();
            for (String name : names) {
                if (OreDictionary.doesOreNameExist(name)) {
                    set.addAll(OreDictionary.getOres(name).stream().map(IBaubleKey.BaubleKey::wrap).collect(Collectors.toList()));
                }
                else {
                    try {
                        set.add(ItemParser.parse(name));
                    } catch (Throwable e) {
                        BaublesApi.log.error("items loading error", e);
                    }
                }
            }
            return set;
        }
    }

    static class GroupKey {
        boolean removed;
        List<BaubleTypeEx> types;

        GroupKey(ItemStack stack) {
            if (BaublesWrapper.Attribute.isRemoved(stack)) {
                this.removed = true;
                this.types = null;
            }
            else {
                this.removed = false;
                this.types = ItemsData.toBauble(stack).getTypes(stack);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupKey groupKey = (GroupKey) o;
            return this.removed == groupKey.removed && Objects.equals(this.types, groupKey.types);
        }

        @Override
        public int hashCode() {
            return Objects.hash(removed, types);
        }
    }

    static class GroupResult {
        List<ItemStack> stacks = new ArrayList<>();
        List<String> names = new ArrayList<>();

        public void add(ItemStack stack) {
            this.stacks.add(stack);
            this.names.add(ItemParser.itemDef(stack));
        }
    }
}
