package baubles.common;

import baubles.common.config.cfg.CfgBaubles;
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
    public static final String CLIENT_GUI = "client.gui";
    protected static Configuration configFile;
    public File modDir;

//    Configuration Options
    public static boolean renderBaubles = true;
    public static boolean baublesButton = true;
    public static boolean baublesTab = false;
    public static boolean jsonFunction = false;
    public static int invPosX = 0;
    public static int babPosX = 28;
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
        renderBaubles = configFile.getBoolean("baubleRender", CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        baublesButton = configFile.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baublesButton or not");
        baublesTab = configFile.getBoolean("baublesTab", CLIENT_GUI, baublesTab, "Show baublesTab or not");
        babPosX = configFile.getInt("babPosX", CLIENT_GUI, babPosX, 0, 255, "The x position of button which calls baublesTab");
        invPosX = configFile.getInt("invPosX", CLIENT_GUI, invPosX, 0, 255, "The x position of button which calls inventory");

        maxLevel = configFile.getInt("maxLevel", CATEGORY_GENERAL, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");

        jsonFunction = configFile.getBoolean("jsonFunction", CATEGORY_GENERAL ,jsonFunction, "Activate json function or not.");

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
//todo configFile ui