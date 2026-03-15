package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.module.IModule;
import baubles.common.config.Config;
import baubles.lib.util.ItemQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class ConversionHelper {

    final static Gson GSON = initGson();
    private static Gson initGson() {
        GsonBuilder builder = new GsonBuilder();
        for (Content content : Content.values()) {
            builder.registerTypeAdapter(content.type, content.adapter);
        }
        return builder.setPrettyPrinting().create();
    }

    public static void toJson(Object o, Content content) throws IOException {
        File output = new File(Config.getModDir(), content.name().toLowerCase() + "_dump.json");
        FileUtils.write(output, GSON.toJson(o, content.type), StandardCharsets.UTF_8);
    }

    public static <T> T fromJson(Content content) throws IOException {
        for (File file : Config.getJson(content)) {
            try {
                return GSON.fromJson(new FileReader(file), content.type);
            } catch (FileNotFoundException e) {
                FileUtils.write(file, null, StandardCharsets.UTF_8, true);
            }
        }
        return null;
    }

    public enum Content {

        ITEMS(ItemDataAdapter.INSTANCE, new TypeToken<List<ItemQuery>>() {}.getType()),
        ITEM(ItemDeserializer.INSTANCE, ItemQuery.class),
        TYPES(TypeDataAdapter.INSTANCE, new TypeToken<List<BaubleTypeEx>>() {}.getType()),
        TYPE(TypeDeserializer.INSTANCE, BaubleTypeEx.class),
        MODULES(ModuleDataAdapter.INSTANCE, new TypeToken<Map<String, IModule>>() {}.getType()),
        MODULE(ModuleDeserializer.INSTANCE, IModule.class)
        ;

        final Type type;
        final Object adapter;

        Content(Object adapter, Type type) {
            this.adapter = adapter;
            this.type = type;
        }
    }
}