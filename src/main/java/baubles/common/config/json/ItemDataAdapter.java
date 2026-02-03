package baubles.common.config.json;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.IBaubleKey;
import baubles.api.registries.ItemData;
import baubles.util.ItemParser;
import baubles.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemDataAdapter extends TypeAdapter<List<IBaubleKey>> {
    static final ItemDataAdapter INSTANCE = new ItemDataAdapter();

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
        JsonUtils.parseObject(in, (reader, name) -> {
            JsonElement json = ConversionHelper.GSON.fromJson(reader, JsonElement.class);
            ItemDeserializer.Temp temp = ConversionHelper.GSON.fromJson(json, Category.ITEM.type);
            if (temp.content.isEmpty()) {
                temp.content.add(name);
            }
            temp.apply();
        });
        return null;
    }

    static class GroupKey {
        boolean removed;
        List<BaubleTypeEx> types;

        GroupKey(ItemStack stack) {
            if (AbstractWrapper.Addition.isRemoved(stack)) {
                this.removed = true;
                this.types = null;
            }
            else {
                this.removed = false;
                this.types = ItemData.toBauble(stack).getTypes(stack);
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
