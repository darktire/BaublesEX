package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TypeDataAdapter extends TypeAdapter<List<BaubleTypeEx>> {
    @Override
    public void write(JsonWriter out, List<BaubleTypeEx> value) throws IOException {
        out.beginObject();
        for (BaubleTypeEx type: value) {
            out.name(String.valueOf(type.getRegistryName()));
            out.beginObject();
            out.name("typeName").value(type.getTypeName());
            out.name("amount").value(type.getAmount());
            out.name("texture").value(type.getTexture());
            out.name("translateKey").value(type.getTranslateKey());
            out.endObject();
        }
        out.endObject();
    }

    @Override
    public List<BaubleTypeEx> read(JsonReader in) throws IOException {
        List<BaubleTypeEx> types = new LinkedList<>();
        in.beginObject();
        while (in.hasNext()) {
            String typeName = null;
            int amount = 1;
            in.nextName();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "typeName": typeName = in.nextString().toLowerCase(); break;
                    case "amount": amount = in.nextInt(); break;
                    default: in.skipValue(); break;
                }
            }
            in.endObject();
            if (typeName!= null) types.add(new BaubleTypeEx(typeName, amount));
        }
        in.endObject();
        return types;
    }
}
