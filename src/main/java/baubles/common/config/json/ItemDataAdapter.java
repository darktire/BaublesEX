package baubles.common.config.json;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.registries.ItemData;
import baubles.lib.util.ItemQuery;
import baubles.lib.util.JsonUtils;
import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemDataAdapter extends TypeAdapter<List<ItemQuery>> {
    static final ItemDataAdapter INSTANCE = new ItemDataAdapter();

    @Override
    public void write(JsonWriter out, List<ItemQuery> value) throws IOException {
        Map<GroupKey, List<String>> map = sort(value);
        out.beginObject();
        int i = 0;
        for (Map.Entry<GroupKey, List<String>> entry : map.entrySet()) {
            out.name("block" + i++);
            out.beginObject();
            GroupKey key = entry.getKey();
            out.name("content");
            out.beginArray();
            for (String name : entry.getValue()) {
                out.value(name);
            }
            out.endArray();
            if (key.removed) {
                out.name("addition").value("remove");
            } else {
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

    private static Map<GroupKey, List<String>> sort(List<ItemQuery> value) {
        return value.stream().collect(
                Collectors.groupingBy(
                        GroupKey::of,
                        Collectors.mapping(ItemQuery::toString, Collectors.toList())
                ));
    }

    @Override
    public List<ItemQuery> read(JsonReader in) throws IOException {
        JsonUtils.objectStream(in, name -> {
            JsonObject obj = ConversionHelper.GSON.fromJson(in, JsonObject.class);
            if (!obj.has("content")) obj.addProperty("content", name);
            ConversionHelper.GSON.fromJson(obj, ConversionHelper.Content.ITEM.type);
        });
        return null;
    }

    @Desugar
    record GroupKey(boolean removed, List<BaubleTypeEx> types) {
        static GroupKey of(ItemQuery query) {
            ItemStack stack = query.ref();
            boolean removed = AbstractWrapper.Addition.isRemoved(stack);
            List<BaubleTypeEx> types = removed ? Collections.emptyList() : ItemData.toBauble(stack).getTypes(stack);
            return new GroupKey(removed, types);
        }
    }
}
