package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeDataAdapter extends TypeAdapter<List<BaubleTypeEx>> {
    static TypeDataAdapter INSTANCE = new TypeDataAdapter();

    @Override
    public void write(JsonWriter out, List<BaubleTypeEx> value) throws IOException {
        out.beginObject();
        for (BaubleTypeEx type: value) {
            out.name(String.valueOf(type.getRegistryName()));
            out.beginObject();
            out.name("name").value(type.getName());
            out.name("amount").value(type.getAmount());
            out.name("priority").value(type.getPriority());
            if (!BaubleTypeEx.isGlobal(type)) {
                out.name("parent");
                out.beginArray();
                for (BaubleTypeEx parent : type.getParents()) {
                    out.value(parent.getRegistryName().toString());
                }
                out.endArray();
            }
            out.name("texture").value(type.getTexture());
            out.name("translateKey").value(type.getTranslateKey());
            out.endObject();
        }
        out.endObject();
    }

    @Override
    public List<BaubleTypeEx> read(JsonReader in) throws IOException {
        List<BaubleTypeEx> list = new ArrayList<>();
        JsonUtils.parseObject(in, (reader, name) -> {
            JsonElement json = ConversionHelper.GSON.fromJson(reader, JsonElement.class);
            TypeDeserializer.Temp temp = ConversionHelper.GSON.fromJson(json, Category.TYPE.type);
            list.add(temp.apply());
        });
        return list;
    }
}
