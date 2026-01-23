package baubles.common.config;

import baubles.api.module.IModule;
import baubles.common.config.json.Category;
import baubles.common.config.json.ConversionHelper;

import java.util.HashMap;
import java.util.Map;

public class ConfigRecord {
    private static final Map<String, IModule> MODULE_MAP = init();
    private static Map<String, IModule> init() {
        try {
            Map<String, IModule> map = ConversionHelper.fromJson(Category.MODULE_DATA);
            if (map == null) map = new HashMap<>();
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IModule getModule(String name) {
        return MODULE_MAP.get(name);
    }
}
