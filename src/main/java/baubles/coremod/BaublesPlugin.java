package baubles.coremod;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Baubles_CoreMod")
@IFMLLoadingPlugin.SortingIndex(2333)
public class BaublesPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public BaublesPlugin() {
        MixinBootstrap.init();
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public List<String> getMixinConfigs() {
        return earlyMixin;
    }

    private static final List<String> earlyMixin = ImmutableList.<String>builder().add("mixins.baubles.early.json").build();
}
