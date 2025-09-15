package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
import baubles.common.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class JsonHelper {

    private final static Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new CustomTypeAdapterFactory())
            .setPrettyPrinting()
            .create();
    private final static File TYPE_JSON = new File(Config.MOD_DIR, "types_data.json");
    private final static File ITEM_JSON = new File(Config.MOD_DIR, "items_data.json");
    private final static File TYPE_DUMP = new File(Config.MOD_DIR, "types_dump.json");
    private final static File ITEM_DUMP = new File(Config.MOD_DIR, "items_dump.json");

    public final static TypeToken<List<BaubleTypeEx>> TOKEN1 = new TypeToken<List<BaubleTypeEx>>() {};
    public final static TypeToken<List<BaublesWrapper>> TOKEN2 = new TypeToken<List<BaublesWrapper>>() {};

    public static void typeToJson(List<BaubleTypeEx> types, boolean dump) throws IOException {
        File output = dump ? TYPE_DUMP : TYPE_JSON;
        FileUtils.write(output, GSON.toJson(types, TOKEN1.getType()), StandardCharsets.UTF_8);
    }

    public static List<BaubleTypeEx> jsonToType() throws IOException {
        try {
            return GSON.fromJson(new FileReader(TYPE_JSON), TOKEN1.getType());
        } catch (FileNotFoundException e) {
            FileUtils.write(TYPE_JSON, null, StandardCharsets.UTF_8, true);
        }
        return null;
    }

    public static void itemToJson(List<BaublesWrapper> items, boolean dump) throws IOException {
        File output = dump ? ITEM_DUMP : ITEM_JSON;
        FileUtils.write(output, GSON.toJson(items, TOKEN2.getType()), StandardCharsets.UTF_8);
    }

    public static List<BaublesWrapper> jsonToItem() throws IOException {
        try {
            return GSON.fromJson(new FileReader(ITEM_JSON), TOKEN2.getType());
        } catch (FileNotFoundException e) {
            FileUtils.write(ITEM_JSON, null, StandardCharsets.UTF_8);
        }
        return null;
    }
}