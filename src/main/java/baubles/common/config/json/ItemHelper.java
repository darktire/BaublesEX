package baubles.common.config.json;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBaubleKey;
import baubles.api.module.IModule;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.common.config.ConfigRecord;
import baubles.util.ItemParser;
import baubles.util.JsonUtils;
import com.google.gson.*;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemHelper implements JsonSerializer<ItemHelper.Temp>, JsonDeserializer<ItemHelper.Temp> {
    static ItemHelper INSTANCE = new ItemHelper();

    @Override
    public JsonElement serialize(Temp src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }

    @Override
    public Temp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();
        Temp temp = new Temp();

        List<String> content = new ArrayList<>();
        JsonUtils.parseArray(in, "content").forEach(e -> content.add(e.getAsString()));
        temp.content = content;

        List<BaubleTypeEx> types = new ArrayList<>();
        JsonUtils.parseArray(in, "types").forEach(e -> {
            BaubleTypeEx type = TypeData.getTypeByName(e.getAsString());
            if (type != null) types.add(type);
        });
        temp.types = types;

        List<IModule> modules = new ArrayList<>();
        JsonUtils.parseArray(in, "modules").forEach(e -> {
            if (e instanceof JsonObject) {
                Object o = ConversionHelper.GSON.fromJson(e, Category.MODULE.type);
                if (o != null) modules.add((IModule) o);
            } else if (e instanceof JsonPrimitive) {
                IModule module = ConfigRecord.getModule(e.getAsString());
                if (module != null) modules.add(module);
            }
        });
        temp.modules = modules;

        JsonUtils.parseArray(in, "addition").forEach(e -> {
            String name = e.getAsString();
            if (name.equals("remove")) {
                temp.remove = true;
            }
        });
        return temp;
    }

    public static final class Temp {
        List<String> content;
        List<BaubleTypeEx> types;
        List<IModule> modules;
        boolean remove;

        void apply() {
            for (IBaubleKey key : getContent(this.content)) {
                ItemData.registerBauble(key, this.types);
                if (!this.modules.isEmpty()) ItemData.registerModules(key, this.modules);
                if (this.remove) AbstractWrapper.CSTMap.instance().update(key, AbstractWrapper.Addition::remove, true);
            }
        }

        static Set<IBaubleKey> getContent(List<String> names) {
            Set<IBaubleKey> set = new HashSet<>();
            for (String name : names) {
                if (OreDictionary.doesOreNameExist(name)) {
                    set.addAll(OreDictionary.getOres(name).stream().map(IBaubleKey.BaubleKey::wrap).collect(Collectors.toList()));
                }
                else {
                    try {
                        set.add(ItemParser.parse(name));
                    } catch (Exception e) {
                        BaublesApi.log.error("items loading error: " + e.getMessage());
                    }
                }
            }
            return set;
        }
    }
}
