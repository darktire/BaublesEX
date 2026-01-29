package baubles.common.config;

import baubles.api.BaublesApi;
import baubles.api.module.IModule;
import baubles.api.registries.ItemData;
import baubles.common.config.json.Category;
import baubles.common.config.json.ConversionHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ConfigRecord implements ISelectiveResourceReloadListener {
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

    public static void reload() {
        try {
            MODULE_MAP.clear();
            Map<String, IModule> map = ConversionHelper.fromJson(Category.MODULE_DATA);
            if (map != null) {
                MODULE_MAP.putAll(map);
            }
            ItemData.restore();
            ConversionHelper.fromJson(Category.ITEM_DATA);
        } catch (Exception e) {
            BaublesApi.log.error("error reloading", e);
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        reload();
    }
}
