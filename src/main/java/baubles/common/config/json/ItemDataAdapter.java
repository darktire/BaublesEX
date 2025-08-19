package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.registries.TypesData;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ItemDataAdapter extends TypeAdapter<List<BaublesWrapper>>  {
    @Override
    public void write(JsonWriter out, List<BaublesWrapper> value) throws IOException {
        out.beginObject();
        for (BaublesWrapper wrapper: value) {
            Item item = wrapper.getItem();
            out.name(item.getRegistryName().toString());
            out.beginObject();
            if (wrapper.getBauble() == null) {
                out.name("addition").value("remove");
            }
            else {
                if (wrapper.isCopy()) {
                    out.name("copyFrom").value(((Item) wrapper.getBauble()).getRegistryName().toString());
                }
                out.name("types");
                out.beginArray();
                for (BaubleTypeEx type: wrapper.getBaubleTypes()) {
                    out.value(type.getRegistryName().toString());
                }
                out.endArray();
            }
            out.endObject();
        }
        out.endObject();

    }

    @Override
    public List<BaublesWrapper> read(JsonReader in) throws IOException {
        List<BaublesWrapper> items = new LinkedList<>();
        in.beginObject();
        while (in.hasNext()) {
            //start item
            Item item = Item.getByNameOrId(in.nextName());
            List<BaubleTypeEx> types = new LinkedList<>();
            in.beginObject();
            while (in.hasNext()) {
                //start item info
                switch (in.nextName()) {
                    case "types":
                        in.beginArray();
                        //start types list
                        while (in.hasNext()) {
                            BaubleTypeEx type = TypesData.getTypeByName(in.nextString());
                            if (type != null) types.add(type);
                        }
                        in.endArray();
                        if (!types.isEmpty()) items.add(new BaublesWrapper(item, new BaubleItem(types)));
                        break;
                    case "copyFrom":
                        Item from = Item.getByNameOrId(in.nextString());
                        if (from instanceof IBauble) items.add(new BaublesWrapper(item, (IBauble) from));
                        break;
                    case "addition":
                        if (in.nextString().equals("remove")) items.add(new BaublesWrapper(item, null));
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
        }
        in.endObject();
        return items;
    }
}
