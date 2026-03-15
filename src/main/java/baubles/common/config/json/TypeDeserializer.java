package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.TypeData;
import baubles.lib.util.JsonUtils;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeDeserializer implements JsonDeserializer<BaubleTypeEx> {
    static TypeDeserializer INSTANCE = new TypeDeserializer();

    @Override
    public BaubleTypeEx deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();
        List<BaubleTypeEx> parents = new ArrayList<>();
        JsonUtils.parseArray(in, "parent").forEach(e -> {
            BaubleTypeEx parent = TypeData.getTypeByName(e.getAsString());
            if (parent != null) parents.add(parent);
        });
        return TypeData.registerType(
                JsonUtils.getString(in, "name"),
                JsonUtils.getInt(in, "amount", 0),
                JsonUtils.getInt(in, "priority", 0),
                parents
        );
    }
}
