package baubles.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private final Deque<ClassLoader> queue = new ArrayDeque<>();
    private static final Logger log = LogManager.getLogger("BAUBLES_MIXIN");

    @Override
    public void onLoad(String mixinPackage) {
        for (ClassLoader cur = this.loader; cur != null; cur = cur.getParent()) {
            queue.addLast(cur);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String modId = MixinInfo.getTargetModId(mixinClassName);
        boolean flag = MixinInfo.isModLoaded(modId, this.queue);
        if (!flag) this.warn(targetClassName, modId);
        return flag;
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

    private void warn(String s1, String s2) {
        log.warn(String.format("Mixin for %s loading failed. If %s wasn't installed, please ignore this warning.", s1, s2));
    }
}
