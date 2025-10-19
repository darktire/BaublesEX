package baubles.compat.config;

import baubles.api.BaublesApi;
import darktire.configearlier.AutoSync;
import darktire.configearlier.ConfigEarlier;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class Compat {
    private static Configuration cfg;
    private static final String BAUBLES_COMPAT = "compat";
    public static boolean aether = true;
    public static boolean botania = true;

    @AutoSync(modid = BaublesApi.MOD_ID, name = BaublesApi.MOD_ID + "_compat", cfg = "cfg")
    public static void load(Configuration cfg) {
        aether = getBoolean(cfg, "aether");
        botania = getBoolean(cfg, "botania");
    }

    private static boolean getBoolean(Configuration cfg, String mod) {
        return cfg.getBoolean(mod, BAUBLES_COMPAT, true, "If true, will apply patches for " + mod);
    }

    public static void addElement(List<IConfigElement> list) {
        if (cfg != null) {
            list.add(new ConfigElement(cfg.getCategory(BAUBLES_COMPAT)));
        }
    }

    static {
        try {
            ConfigEarlier.register(Compat.class);
        } catch (Throwable ignore) {}
    }
}
