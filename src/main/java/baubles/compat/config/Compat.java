package baubles.compat.config;

import baubles.common.config.Config;
import baubles.common.config.PartialConfig;
import net.minecraftforge.common.config.Configuration;

public class Compat extends PartialConfig {
    private static final String BAUBLES_COMPAT = "general.compat";
    public static boolean aether;
    public static boolean botania;

    public void loadData() {
        Configuration cfg = Config.getInstance();
        aether = getBoolean(cfg, "aether");
        botania = getBoolean(cfg, "botania");
    }

    private static boolean getBoolean(Configuration cfg, String mod) {
        return cfg.getBoolean(mod, BAUBLES_COMPAT, true, "If true, will apply patches for " + mod);
    }

    static {
        PartialConfig.create(Compat.class);
    }
}
