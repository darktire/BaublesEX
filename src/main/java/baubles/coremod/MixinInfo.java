package baubles.coremod;

import baubles.util.HookHelper;
import net.minecraftforge.fml.common.Loader;

public class MixinInfo {

    public static boolean isModLoaded(String modId) {
        if (modId.equals("artifacts")) return HookHelper.isModLoaded("RLArtifacts");
        return Loader.isModLoaded(modId);
    }

    public static String getTargetModId(String mixinClassName) {
        String substring = mixinClassName.substring(19);
        return substring.substring(0, substring.indexOf('.'));
    }
}
