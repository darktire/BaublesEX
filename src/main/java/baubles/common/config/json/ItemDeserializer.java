package baubles.common.config.json;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.IBaubleKey;
import baubles.api.module.IModule;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.render.ArmorHelper;
import baubles.common.config.ConfigRecord;
import baubles.util.ItemParser;
import baubles.util.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ItemDeserializer implements JsonDeserializer<ItemDeserializer.Temp> {
    static ItemDeserializer INSTANCE = new ItemDeserializer();

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

        List<Pair<IBaubleKey, AbstractWrapper.Sample>> samples = new ArrayList<>();
        JsonUtils.parseArray(in, "addition").forEach(e -> {
            String name = e.getAsString();

            if (name.equals("remove")) {
                temp.remove = true;
            }

            String[] args = name.split("#", 2);
            String feature = args[0];
            String itemName = args.length > 1 ? args[1] : null;

            IBaubleKey key = Optional.ofNullable(itemName)
                    .filter(s -> !s.isEmpty())
                    .map(ItemDeserializer::parseItem)
                    .orElse(null);

            fromStr(feature).stream()
                    .map(sample -> solvePair(temp, key, sample))
                    .forEach(samples::add);
        });
        temp.samples = samples;
        return temp;
    }

    private static ImmutablePair<IBaubleKey, AbstractWrapper.Sample> solvePair(Temp temp, IBaubleKey key, AbstractWrapper.Sample sample) {
        if (key == null && sample == AbstractWrapper.Sample.ARMOR) {
            temp.render = ArmorHelper.INSTANCE;
        }
        return new ImmutablePair<>(key, sample);
    }

    private static IBaubleKey parseItem(String itemName) {
        try {
            return ItemParser.parse(itemName);
        } catch (Exception e) {
            BaublesApi.log.error("items loading error: " + e.getMessage());
            return IBaubleKey.EMPTY;
        }
    }

    private static final Map<Character, AbstractWrapper.Sample> CODE_MAP = ImmutableMap.of(
            'A', AbstractWrapper.Sample.ARMOR,
            'P', AbstractWrapper.Sample.PASSIVE,
            'I', AbstractWrapper.Sample.IN_USE
    );
    private static AbstractWrapper.Sample fromCode(char code) {
        return CODE_MAP.get(Character.toUpperCase(code));
    }
    private static List<AbstractWrapper.Sample> fromStr(String str) {
        return str.chars()
                .mapToObj(c -> fromCode((char) c))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public static final class Temp {
        private static final AbstractWrapper.CSTMap CST_MAP = AbstractWrapper.CSTMap.instance();
        List<String> content;
        List<BaubleTypeEx> types;
        List<IModule> modules;
        boolean remove;
        List<Pair<IBaubleKey, AbstractWrapper.Sample>> samples;
        IRenderBauble render;

        void apply() {
            for (IBaubleKey key : getContent(this.content)) {
                ItemData.registerBauble(key, this.types);
                if (!this.modules.isEmpty()) CST_MAP.update(key, AbstractWrapper.Addition::modules, this.modules);
                if (this.remove) CST_MAP.update(key, AbstractWrapper.Addition::remove, true);
                if (!this.samples.isEmpty()) CST_MAP.update(key, AbstractWrapper.Addition::samples, this.samples);
                if (this.render != null) CST_MAP.update(key, AbstractWrapper.Addition::render, this.render);
            }
        }

        static Set<IBaubleKey> getContent(List<String> names) {
            Set<IBaubleKey> set = new HashSet<>();
            for (String name : names) {
                if (OreDictionary.doesOreNameExist(name)) {
                    set.addAll(OreDictionary.getOres(name).stream().map(IBaubleKey.BaubleKey::wrap).collect(Collectors.toList()));
                }
                else {
                    set.add(parseItem(name));
                }
            }
            return set;
        }
    }
}
