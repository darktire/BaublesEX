package baubles.common;

import baubles.common.config.cfg.CfgBaubles;
import baubles.common.config.cfg.CfgGui;
import baubles.common.extra.BaublesContent;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;

import static baubles.common.Baubles.config;
import static net.minecraftforge.common.config.Configuration.CATEGORY_CLIENT;
import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
    protected static Configuration configFile;
    public File modDir;

//    Configuration Options
    public static boolean renderBaubles = true;
    public static boolean jsonFunction = false;
    public static boolean trinketLimit = false;
    public static boolean keepBaubles = false;
    public static int maxLevel = 1;
    protected static String[] clickBlacklist = new String[0];

    public Config(FMLPreInitializationEvent event) {
        loadConfig(event);
        MinecraftForge.EVENT_BUS.register(Config.ConfigChangeListener.class);
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        modDir = event.getModConfigurationDirectory();
        try {
            configFile = new Configuration(event.getSuggestedConfigurationFile());
            configFile.load();
            loadData();
        } catch (Exception e) {
            Baubles.log.error("BAUBLES has a problem loading it's configuration");
        } finally {
            if (Config.configFile != null) configFile.save();
        }
    }


    protected void loadData() {

        new CfgBaubles(configFile);
        new CfgGui(configFile);

        renderBaubles = configFile.getBoolean("baubleRender", CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        maxLevel = configFile.getInt("maxLevel", CATEGORY_GENERAL, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");

        jsonFunction = configFile.getBoolean("jsonFunction", CATEGORY_GENERAL ,jsonFunction, "Activate json function or not. (experimental function)");

        trinketLimit = configFile.getBoolean("trinketLimit", CATEGORY_GENERAL, trinketLimit, "Whether trinketSlot is independent. If true, trinket will become a independent type.");
        keepBaubles = configFile.getBoolean("keepBaubles", CATEGORY_GENERAL, keepBaubles, "Whether baubles can drop when player dies.");

        clickBlacklist = configFile.getStringList("clickBlacklist", CATEGORY_GENERAL, clickBlacklist, "");

        configFile.save();
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

    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(Baubles.MODID)) {
                config.loadData();
                Baubles.baubles = new BaublesContent();
                if (jsonFunction) {
                    Baubles.baubles.writeJson();
                }
            }
        }
    }
}
//exposed as old api