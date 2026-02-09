package baubles.compat.config;

import baubles.common.config.Config;
import baubles.common.config.PartialConfig;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class Compat extends PartialConfig {
    private static final String BAUBLES_COMPAT = "general.compat";
    private static final Map<String, Boolean> options = new HashMap<>();

    public void loadData() {
        Configuration cfg = Config.getInstance();
        cfg.getCategory(BAUBLES_COMPAT).setComment("Toggle compatibility modifications");
    }

    private static boolean getBoolean(Configuration cfg, String mod) {
        return cfg.getBoolean(mod, BAUBLES_COMPAT, true, "If true, will apply patches for " + mod);
    }

    public static boolean getConfig(String mod) {
        return options.computeIfAbsent(mod, key -> getBoolean(Config.getInstance(), key));
    }

    static {
        PartialConfig.create(Compat.class);
    }
}
