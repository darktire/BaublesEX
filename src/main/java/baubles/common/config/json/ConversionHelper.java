package baubles.common.config.json;

import baubles.common.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class ConversionHelper {

    final static Gson GSON = initGson();
    private static Gson initGson() {
        GsonBuilder builder = new GsonBuilder();
        for (Category category : Category.values()) {
            builder.registerTypeAdapter(category.type, category.adapter);
        }
        return builder.setPrettyPrinting().create();
    }

    private final static File TYPE_JSON = new File(Config.getModDir(), "types_data.json");
    private final static File ITEM_JSON = new File(Config.getModDir(), "items_data.json");
    private final static File TYPE_DUMP = new File(Config.getModDir(), "types_dump.json");
    private final static File ITEM_DUMP = new File(Config.getModDir(), "items_dump.json");

    public static void toJson(Object o, Category category) throws IOException {
        File output = category == Category.ITEM_DATA ? ITEM_DUMP : TYPE_DUMP;
        FileUtils.write(output, GSON.toJson(o, category.type), StandardCharsets.UTF_8);
    }

    public static <T> T fromJson(Category category) throws IOException {
        for (File file : Config.getJson(category)) {
            try {
                return GSON.fromJson(new FileReader(file), category.type);
            } catch (FileNotFoundException e) {
                FileUtils.write(file, null, StandardCharsets.UTF_8, true);
            }
        }
        return null;
    }
}