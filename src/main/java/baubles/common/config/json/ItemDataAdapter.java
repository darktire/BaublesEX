package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.api.IWrapper;
import baubles.api.cap.BaubleItem;
import baubles.api.registries.ItemsData;
import baubles.api.registries.TypesData;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ItemDataAdapter extends TypeAdapter<List<IWrapper>>  {

    private static final BaublesWrapper.CSTMap CST_MAP = BaublesWrapper.CSTMap.instance();

    @Override
    public void write(JsonWriter out, List<IWrapper> value) throws IOException {
        out.beginObject();
        for (IWrapper wrapper: value) {
            Item item = wrapper.getItemStack().getItem();
            out.name(item.getRegistryName().toString());
            out.beginObject();
            if (BaublesWrapper.CSTMap.isRemoved(item)) {
                out.name("addition").value("remove");
            }
            else {
                out.name("types");
                out.beginArray();
                for (BaubleTypeEx type : wrapper.getTypes(null)) {
                    out.value(type.getRegistryName().toString());
                }
                out.endArray();
            }
            out.endObject();
        }
        out.endObject();

    }

    @Override
    public List<IWrapper> read(JsonReader in) throws IOException {
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
                        if (!types.isEmpty() && item != null) ItemsData.registerBauble(item, new BaubleItem(types));
                        break;
                    case "addition":
                        if (in.nextString().equals("remove")) CST_MAP.update(item, attribute -> attribute.remove(true));
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
        }
        in.endObject();
        return null;
    }
}
