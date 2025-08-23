package baubles.mixin;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MixinInfo {
    public static boolean isModLoaded(String modId, ClassLoader loader) {
        if (modId.equals("baubles")) return true;
        else {
            String className = modMainClass.get(modId);
            if (className != null) {
                try {
                    Class.forName(className, false, loader);
                    return true;
                }
                catch (ClassNotFoundException ignored) {}
            }
            return false;
        }
    }

    public static String getTargetModId(String mixinClassName) {
        return mixinTargetMod.get(mixinClassName);
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
            .put("mobends", "goblinbob.mobends.standard.main.MoBends")
            .put("wings", "me.paulf.wings.WingsMod")
            .build();
}
