package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.TypesData;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeDataAdapter extends CustomAdapter<List<BaubleTypeEx>> {
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

        parseObject(in, (reader, key) -> {
            list.add(parseType(reader));
        });
        
        return list;
    }

    private BaubleTypeEx parseType(JsonReader in) throws IOException {
        Temp temp = new Temp();
        parseObject(in, (unused, key) -> {
            switch (key) {
                case "name": temp.name = in.nextString().toLowerCase(); break;
                case "amount": temp.amount = in.nextInt(); break;
                case "priority": temp.priority = in.nextInt(); break;
                case "parent":
                    List<BaubleTypeEx> parents = new ArrayList<>();
                    parseArray(in, typeName -> {
                        BaubleTypeEx parent = TypesData.getTypeByName(typeName);
                        if (parent != null) parents.add(parent);
                    });
                    temp.parents = parents;
                    break;
                default: in.skipValue(); break;
            }
        });
        return temp.apply();
    }

    static final class Temp {
        String name;
        int amount, priority;
        List<BaubleTypeEx> parents;

        BaubleTypeEx apply() {
            return TypesData.registerType(name, amount, priority, parents);
        }
    }
}
