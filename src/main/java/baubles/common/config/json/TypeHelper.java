package baubles.common.config.json;

import baubles.api.BaubleTypeEx;
import baubles.api.registries.TypeData;
import baubles.util.JsonUtils;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeHelper implements JsonSerializer<TypeHelper.Temp>, JsonDeserializer<TypeHelper.Temp> {
    static TypeHelper INSTANCE = new TypeHelper();

    @Override
    public JsonElement serialize(TypeHelper.Temp src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }

    @Override
    public Temp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();
        Temp temp = new Temp();
        temp.name = JsonUtils.getString(in, "name");
        temp.amount = JsonUtils.getInt(in, "amount");
        temp.priority = JsonUtils.getInt(in, "priority");
        List<BaubleTypeEx> parents = new ArrayList<>();
        JsonUtils.parseArray(in, "parent").forEach(e -> {
            BaubleTypeEx parent = TypeData.getTypeByName(e.getAsString());
            if (parent != null) parents.add(parent);
        });
        temp.parents = parents;
        return temp;
    }

    public static final class Temp {
        String name;
        int amount, priority;
        List<BaubleTypeEx> parents;

        BaubleTypeEx apply() {
            return TypeData.registerType(name, amount, priority, parents);
        }
    }
}
