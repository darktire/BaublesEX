package baubles.coremod;

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

    private static final int PRE = "mixins/late/mixins.baubles.".length();
    private static final int SUF = ".json".length();

    private final List<String> lateMixin = ImmutableList.of(
            "mixins/late/mixins.baubles.aether_legacy.json",
            "mixins/late/mixins.baubles.ancientspellcraft.json",
            "mixins/late/mixins.baubles.artifacts.json",
            "mixins/late/mixins.baubles.backpacked.json",
            "mixins/late/mixins.baubles.botania.json",
            "mixins/late/mixins.baubles.bountifulbaubles.json",
            "mixins/late/mixins.baubles.ebwizardry.json",
            "mixins/late/mixins.baubles.enigmaticlegacy.json",
            "mixins/late/mixins.baubles.extrabotany.json",
            "mixins/late/mixins.baubles.iceandfire.json",
            "mixins/late/mixins.baubles.mobends.json",
            "mixins/late/mixins.baubles.nvg.json",
            "mixins/late/mixins.baubles.potionfingers.json",
            "mixins/late/mixins.baubles.rlartifacts.json",
            "mixins/late/mixins.baubles.setbonus.json",
            "mixins/late/mixins.baubles.thaumcraft.json",
            "mixins/late/mixins.baubles.thaumicperiphery.json",
            "mixins/late/mixins.baubles.tombstone.json",
            "mixins/late/mixins.baubles.wings.json",
            "mixins/late/mixins.baubles.wizardryutils.json",
            "mixins/late/mixins.baubles.xat.json"
    );

    @Override
    public List<String> getMixinConfigs() {
        return lateMixin.stream().filter(this::fromJson).collect(Collectors.toList());
    }

    private boolean fromJson(String name) {
        String modid = name.substring(PRE, name.length() - SUF);
        return HookHelper.isModLoaded(modid);
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
        return HookHelper.getConfig(modid);
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
