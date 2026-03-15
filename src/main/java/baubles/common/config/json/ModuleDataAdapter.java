package baubles.common.config.json;

import baubles.api.module.IModule;
import baubles.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModuleDataAdapter extends TypeAdapter<Map<String, IModule>> {
    static ModuleDataAdapter INSTANCE = new ModuleDataAdapter();

    @Override
    public void write(JsonWriter out, Map<String, IModule> value) throws IOException {

    }

    @Override
    public Map<String, IModule> read(JsonReader in) throws IOException {
        Map<String, IModule> map = new HashMap<>();
        JsonUtils.objectStream(in, name -> {
            JsonElement effectJson = ConversionHelper.GSON.fromJson(in, JsonElement.class);
            IModule module = ConversionHelper.GSON.fromJson(effectJson, ConversionHelper.Content.MODULE.type);
            map.put(name, module);
        });
        return map;
    }
}