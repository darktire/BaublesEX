package baubles.common.config.json;

import baubles.api.AbstractWrapper;
import baubles.api.BaubleTypeEx;
import baubles.api.BaublesApi;
import baubles.api.module.IModule;
import baubles.api.registries.ItemData;
import baubles.api.registries.TypeData;
import baubles.api.render.IRenderBauble;
import baubles.client.render.ArmorHelper;
import baubles.common.config.ConfigRecord;
import baubles.lib.util.ItemQuery;
import baubles.lib.util.JsonUtils;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ItemDeserializer implements JsonDeserializer<ItemQuery> {
    public static final ItemDeserializer INSTANCE = new ItemDeserializer();

    @Override
    public ItemQuery deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject in = json.getAsJsonObject();

        List<BaubleTypeEx> types = JsonUtils.parseArray(in, "types").stream()
                .map(e -> TypeData.getTypeByName(e.getAsString()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<IModule> modules = JsonUtils.parseArray(in, "modules").stream()
                .map(ItemDeserializer::parseModule)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        boolean remove = false;
        IRenderBauble render = null;
        List<AbstractWrapper.WornTickEffect> effects = new ArrayList<>();

        for (JsonElement e : JsonUtils.parseArray(in, "addition")) {
            String name = e.getAsString();
            if (name.equals("remove")) {
                remove = true;
                continue;
            }

            String[] args = name.split("#", 2);
            String feature = args[0];
            ItemQuery query = args.length > 1 ? parseItem(args[1]) : null;

            List<AbstractWrapper.WornTickEffect> resolved = fromStr(feature, query);
            effects.addAll(resolved);
            if (query == null && feature.chars().anyMatch(c -> Character.toUpperCase(c) == 'A')) {
                render = ArmorHelper.INSTANCE;
            }
        }

        AbstractWrapper.CSTMap cstMap = AbstractWrapper.CSTMap.instance();
        for (ItemQuery key : resolveContent(in)) {
            ItemData.registerBauble(key, types);
            if (!modules.isEmpty()) cstMap.update(key, AbstractWrapper.Addition::modules, modules);
            if (remove) cstMap.update(key, AbstractWrapper.Addition::remove, true);
            if (!effects.isEmpty()) cstMap.update(key, AbstractWrapper.Addition::effects, effects);
            if (render != null) cstMap.update(key, AbstractWrapper.Addition::render, render);
        }

        return null;
    }

    private static Set<ItemQuery> resolveContent(JsonObject in) {
        return JsonUtils.parseArray(in, "content").stream()
                .map(JsonElement::getAsString)
                .flatMap(name -> {
                    if (OreDictionary.doesOreNameExist(name)) {
                        return ItemQuery.ofOreDict(name).stream();
                    }
                    try {
                        return Stream.of(ItemQuery.parse(name));
                    } catch (Exception e) {
                        BaublesApi.log.error(e);
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toSet());
    }

    private static IModule parseModule(JsonElement e) {
        if (e instanceof JsonObject) {
            return ConversionHelper.GSON.fromJson(e, ConversionHelper.Content.MODULE.type);
        } else if (e instanceof JsonPrimitive) {
            return ConfigRecord.getModule(e.getAsString());
        }
        return null;
    }

    private static ItemQuery parseItem(String itemName) {
        if (itemName == null || itemName.isEmpty()) return null;
        try {
            return ItemQuery.parse(itemName);
        } catch (Exception e) {
            BaublesApi.log.error("items loading error: " + e.getMessage());
            return ItemQuery.EMPTY;
        }
    }

    private static final Map<Character, java.util.function.Function<ItemQuery, AbstractWrapper.WornTickEffect>> CODE_MAP =
            ImmutableMap.of(
                    'A', AbstractWrapper.WornTickEffect::armor,
                    'P', AbstractWrapper.WornTickEffect::passive,
                    'I', AbstractWrapper.WornTickEffect::inUse
            );

    private static List<AbstractWrapper.WornTickEffect> fromStr(String str, ItemQuery query) {
        return str.chars()
                .mapToObj(c -> CODE_MAP.get(Character.toUpperCase((char) c)))
                .filter(Objects::nonNull)
                .distinct()
                .map(factory -> factory.apply(query))
                .collect(Collectors.toList());
    }
}
