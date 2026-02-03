package baubles.common.config.json;

import baubles.api.module.IModule;
import baubles.common.module.ModuleAttribute;
import baubles.common.module.ModulePotion;
import com.google.gson.*;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.UUID;

public class ModuleDeserializer implements JsonDeserializer<IModule> {
    static ModuleDeserializer INSTANCE = new ModuleDeserializer();

    @Override
    public IModule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();
        String type = JsonUtils.getString(in, "module", "");
        if (type.equals("attribute") && hasFields(in, "uuid", "attrName", "perLevel", "operation")) {
            return ModuleAttribute.ofName(
                    UUID.fromString(JsonUtils.getString(in, "uuid")),
                    JsonUtils.getString(in, "attrName"),
                    JsonUtils.getFloat(in, "perLevel"),
                    ModuleAttribute.Opt.values()[JsonUtils.getInt(in, "operation")]
            );
        } else if (type.equals("potion") && hasFields(in, "potionName", "perLevel", "limit")) {
            return ModulePotion.of(
                    JsonUtils.getString(in, "potionName"),
                    JsonUtils.getInt(in, "perLevel"),
                    JsonUtils.getInt(in, "limit")
            );
        }
        return null;
    }

    public static boolean hasFields(JsonObject json, String... members) {
        if (json == null) {
            return false;
        }
        else {
            return Arrays.stream(members).allMatch(json::has);
        }
    }
}