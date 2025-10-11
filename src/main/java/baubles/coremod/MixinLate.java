package baubles.coremod;

import baubles.util.HookHelper;
import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.Set;

public class MixinLate implements IMixinConfigPlugin, ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return lateMixin;
    }

    private static final List<String> lateMixin = ImmutableList.<String>builder().add("mixins.baubles.late.json").build();

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String modid = HookHelper.getTargetModId(mixinClassName);
        return HookHelper.isModLoaded(modid) && HookHelper.doApplyMixin(modid);
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
