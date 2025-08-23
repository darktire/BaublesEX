package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.BaublesWrapper;
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

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new CustomTypeAdapterFactory())
            .setPrettyPrinting()
            .create();
    private final File typeJson;
    private final File itemJson;

    public final TypeToken<List<BaubleTypeEx>> token1 = new TypeToken<List<BaubleTypeEx>>() {};
    public final TypeToken<List<BaublesWrapper>> token2 = new TypeToken<List<BaublesWrapper>>() {};

    public JsonHelper(File modDir) {
        File jsonDir = new File(modDir, "baubles");
        this.typeJson = new File(jsonDir, "types_data.json");
        this.itemJson = new File(jsonDir, "items_data.json");
    }

    public void typeToJson(List<BaubleTypeEx> types) throws IOException {
        FileUtils.write(this.typeJson, GSON.toJson(types, this.token1.getType()), StandardCharsets.UTF_8);
    }

    public List<BaubleTypeEx> jsonToType() throws IOException {
        try {
            return GSON.fromJson(new FileReader(this.typeJson), token1.getType());
        } catch (FileNotFoundException e) {
            FileUtils.write(this.typeJson, null, StandardCharsets.UTF_8, true);
        }
        return null;
    }

    public void itemToJson(List<BaublesWrapper> items) throws IOException {
        FileUtils.write(this.itemJson, GSON.toJson(items, this.token2.getType()), StandardCharsets.UTF_8);
    }

    public List<BaublesWrapper> jsonToItem() throws IOException {
        try {
            return GSON.fromJson(new FileReader(this.itemJson), this.token2.getType());
        } catch (FileNotFoundException e) {
            FileUtils.write(this.itemJson, null, StandardCharsets.UTF_8);
        }
        return null;
    }
}