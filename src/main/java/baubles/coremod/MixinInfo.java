package baubles.coremod;

import baubles.util.HookHelper;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

public class MixinInfo {

    public static boolean isModLoaded(String modId) {
        if (modId.equals("artifacts")) return HookHelper.isModLoaded("RLArtifacts");
        return Loader.isModLoaded(modId);
    }

    public static String getTargetModId(String mixinClassName) {
        return modIds.stream().filter(id -> mixinClassName.contains(id + ".")).findAny().orElse(null);
    }

    private static final List<String> modIds = ImmutableList.<String>builder()
            .add("artifacts", "extrautils2", "mobends", "wings")
            .build();
}
