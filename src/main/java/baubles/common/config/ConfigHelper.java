package baubles.common.config;

import baubles.api.BaubleType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Field;

public class ConfigHelper extends Config{
    public ConfigHelper(FMLPreInitializationEvent event) {
        super(event);
    }

    /**
     * Reflect from default types to config.
     * Wait to add json support.
     */
    public int getAmount(String name) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = Config.class;
        Field field = clazz.getDeclaredField(name);
        if (name.equals(BaubleType.TRINKET.name())) return 0;
        return field.getInt(null);
    }

    public Configuration getConfigFile() {
        return configFile;
    }
}
