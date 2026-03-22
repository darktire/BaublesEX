package baubles.coremod;

import baubles.compat.config.Compat;
import baubles.util.HookHelper;
import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MixinLate implements IMixinConfigPlugin, ILateMixinLoader {

    private final String format = "mixins/late/mixins.baubles.%s.json";

    private final List<String> lateMixin = ImmutableList.of(
            "aether_legacy",
            "ancientspellcraft",
            "artifacts",
            "backpacked",
            "botania",
            "bountifulbaubles",
            "colytra",
            "da",
            "ebwizardry",
            "enigmaticlegacy",
            "extrabotany",
            "iceandfire",
            "improvedbackpacks",
            "mobends",
            "nvg",
            "potionfingers",
            "qualitytools",
            "rlartifacts",
            "setbonus",
            "thaumcraft",
            "thaumicperiphery",
            "tombstone",
            "uniquee",
            "uniqueeutil",
            "wearablebackpacks",
            "wings",
            "wizardryutils",
            "xat"
    );

    @Override
    public List<String> getMixinConfigs() {
        return lateMixin.stream()
                .filter(HookHelper::isModLoaded)
                .map(name -> String.format(format, name))
                .collect(Collectors.toList());
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String modid = fromMixin(mixinClassName);
        return Compat.getConfig(modid);
    }

    private String fromMixin(String mixinClassName) {
        String substring = mixinClassName.substring(19);
        return substring.substring(0, substring.indexOf('.'));
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
