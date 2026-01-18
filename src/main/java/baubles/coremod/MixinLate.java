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
    @Override
    public List<String> getMixinConfigs() {
        return lateMixin.stream().filter(this::fromJson).collect(Collectors.toList());
    }

    private boolean fromJson(String name) {
        String modid = name.substring(20, name.length() - 5);
        return HookHelper.isModLoaded(modid);
    }

    private final ImmutableList<String> lateMixin = ImmutableList.of(
            "mixins.baubles.late.aether_legacy.json",
            "mixins.baubles.late.ancientspellcraft.json",
            "mixins.baubles.late.artifacts.json",
            "mixins.baubles.late.backpacked.json",
            "mixins.baubles.late.botania.json",
            "mixins.baubles.late.bountifulbaubles.json",
            "mixins.baubles.late.ebwizardry.json",
            "mixins.baubles.late.enigmaticlegacy.json",
            "mixins.baubles.late.extrabotany.json",
            "mixins.baubles.late.iceandfire.json",
            "mixins.baubles.late.mobends.json",
            "mixins.baubles.late.nvg.json",
            "mixins.baubles.late.rlartifacts.json",
            "mixins.baubles.late.setbonus.json",
            "mixins.baubles.late.thaumcraft.json",
            "mixins.baubles.late.thaumicperiphery.json",
            "mixins.baubles.late.tombstone.json",
            "mixins.baubles.late.wings.json",
            "mixins.baubles.late.wizardryutils.json",
            "mixins.baubles.late.xat.json"
    );

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
