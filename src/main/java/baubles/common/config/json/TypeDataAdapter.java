package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.TypesData;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeDataAdapter extends TypeAdapter<List<BaubleTypeEx>> {
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
        in.beginObject();
        while (in.hasNext()) {
            String name = null;
            int amount = 0, priority = 0;
            List<BaubleTypeEx> parents = new ArrayList<>();
            in.nextName();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "name": name = in.nextString().toLowerCase(); break;
                    case "amount": amount = in.nextInt(); break;
                    case "priority": priority = in.nextInt(); break;
                    case "parent":
                        in.beginArray();
                        while (in.hasNext()) {
                            BaubleTypeEx parent = TypesData.getTypeByName(in.nextString());
                            if (parent != null) parents.add(parent);
                        }
                        in.endArray();
                        break;
                    default: in.skipValue(); break;
                }
            }
            in.endObject();
            list.add(TypesData.registerType(name, amount, priority, parents));
        }
        in.endObject();
        return list;
    }
}
