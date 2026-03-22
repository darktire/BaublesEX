package baubles.common.config.json;

import baubles.api.module.IModule;
import baubles.common.module.ModuleAttribute;
import baubles.common.module.ModulePotion;
import baubles.lib.util.AttrOpt;
import baubles.lib.util.JsonUtils;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class ModuleDeserializer implements JsonDeserializer<IModule> {
    static ModuleDeserializer INSTANCE = new ModuleDeserializer();

    @Override
    public IModule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();
        String type = JsonUtils.getString(in, "module", "");
        if (type.equals("attribute") && JsonUtils.hasFields(in, "uuid", "attrName", "perLevel", "operation")) {
            return ModuleAttribute.ofName(
                    UUID.fromString(JsonUtils.getString(in, "uuid")),
                    JsonUtils.getString(in, "attrName"),
                    JsonUtils.getFloat(in, "perLevel"),
                    AttrOpt.values()[JsonUtils.getInt(in, "operation")]
            );
        } else if (type.equals("potion") && JsonUtils.hasFields(in, "potionName", "perLevel", "limit")) {
            return ModulePotion.of(
                    JsonUtils.getString(in, "potionName"),
                    JsonUtils.getInt(in, "perLevel"),
                    JsonUtils.getInt(in, "limit")
            );
        }
        return null;
    }
}