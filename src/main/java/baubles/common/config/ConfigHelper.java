package baubles.common.config;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
        return field.getInt(null);
    }

    public Configuration getConfigFile() {
        return configFile;
    }

    public ArrayList<Item> blacklistItem() {
        ArrayList<Item> blacklist = new ArrayList<>(clickBlacklist.length);
        for (String s : clickBlacklist) {
            Item item = Item.getByNameOrId(s);
            if (item != null) blacklist.add(item);
        }
        return blacklist;
    }
}
