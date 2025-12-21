package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.IBaubleKey;
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
import java.util.ArrayList;
import java.util.List;


public class ConversionHelper {

    private final static Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new CustomAdapterFactory())
            .setPrettyPrinting()
            .create();
    private final static File TYPE_JSON = new File(Config.getModDir(), "types_data.json");
    private final static File ITEM_JSON = new File(Config.getModDir(), "items_data.json");
    private final static File TYPE_DUMP = new File(Config.getModDir(), "types_dump.json");
    private final static File ITEM_DUMP = new File(Config.getModDir(), "items_dump.json");

    public static int ITEM = 0;
    public static int TYPE = 1;

    public final static TypeToken<List<BaubleTypeEx>> TOKEN1 = new TypeToken<List<BaubleTypeEx>>() {};
    public final static TypeToken<List<IBaubleKey>> TOKEN2 = new TypeToken<List<IBaubleKey>>() {};

    public static void typeToJson(List<BaubleTypeEx> types, boolean dump) throws IOException {
        File output = dump ? TYPE_DUMP : TYPE_JSON;
        FileUtils.write(output, GSON.toJson(types, TOKEN1.getType()), StandardCharsets.UTF_8);
    }

    public static <T> List<T> jsonToType(Class<T> clazz) throws IOException {
        for (File file : Config.getJson(1)) {
            try {
                return GSON.fromJson(new FileReader(file), TOKEN1.getType());
            } catch (FileNotFoundException e) {
                FileUtils.write(file, null, StandardCharsets.UTF_8, true);
            }
        }
        return new ArrayList<>();
    }

    public static void itemToJson(List<IBaubleKey> items, boolean dump) throws IOException {
        File output = dump ? ITEM_DUMP : ITEM_JSON;
        FileUtils.write(output, GSON.toJson(items, TOKEN2.getType()), StandardCharsets.UTF_8);
    }

    public static void jsonToItem() throws IOException {
        for (File file : Config.getJson(0)) {
            try {
                GSON.fromJson(new FileReader(file), TOKEN2.getType());
            } catch (FileNotFoundException e) {
                FileUtils.write(file, null, StandardCharsets.UTF_8);
            }
        }
    }
}
//todo