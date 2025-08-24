package baubles.mixin;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MixinInfo {

    public static boolean isModLoaded(String modId, Deque<ClassLoader> queue) {
        Boolean flag = isModLoaded.get(modId);
        if (flag == null) {
            String className = modMainClass.get(modId);
            if (className != null) {
                flag = exists(className, queue);
                isModLoaded.put(modId, flag);
                return flag;
            }
            return false;
        }
        return flag;
    }

    public static String getTargetModId(String mixinClassName) {
        return mixinTargetMod.get(mixinClassName);
    }

    private static boolean exists(String className, Deque<ClassLoader> queue) {
        String resourceName = className.replace('.', '/') + ".class";
        return queue.stream().anyMatch(loader -> {
            if (loader.getResource(resourceName) != null) {
                return true;
            }

            if (loader instanceof URLClassLoader) {
                try {
                    Enumeration<URL> urls = ((URLClassLoader) loader).findResources(resourceName);
                    if (urls != null && urls.hasMoreElements()) {
                        return true;
                    }
                } catch (IOException ignored) {}
            }
            return false;
        });
    }

    private static final Map<String, Boolean> isModLoaded = new HashMap<>();
    static {
        isModLoaded.put("baubles", true);
    }

    private static final Map<String, String> mixinTargetMod = ImmutableMap.<String, String>builder()
            .put("baubles.mixin.vanilla.MixinElytra$MixinEntityBase", "baubles")
            .put("baubles.mixin.vanilla.MixinElytra$MixinNetHandler", "baubles")
            .put("baubles.mixin.vanilla.MixinEnchantment", "baubles")
            .put("baubles.mixin.vanilla.MixinPlayer", "baubles")
            .put("baubles.mixin.vanilla.MixinStack", "baubles")
            .put("baubles.mixin.vanilla.MixinElytra$MixinCape", "baubles")
            .put("baubles.mixin.vanilla.MixinElytra$MixinLayer", "baubles")
            .put("baubles.mixin.vanilla.MixinElytra$MixinPlayerSP", "baubles")
            .put("baubles.mixin.vanilla.MixinCapability", "baubles")
            .put("baubles.mixin.mobends.MixinCustom", "mobends")
            .put("baubles.mixin.wings.MixinWings", "wings")
            .build();

    private static final Map<String, String> modMainClass = ImmutableMap.<String, String>builder()
            .put("mobends", "goblinbob.mobends.standard.main.ModStatics")
            .put("wings", "me.paulf.wings.WingsMod")
            .build();
}
