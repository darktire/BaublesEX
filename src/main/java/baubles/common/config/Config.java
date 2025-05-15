package baubles.common.config;

import baubles.api.BaubleType;
import baubles.common.Baubles;
import baubles.common.extra.BaublesContent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

import static baubles.common.Baubles.config;
import static net.minecraftforge.common.config.Configuration.CATEGORY_CLIENT;
import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
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
    public static int AMULET;
    public static int RING;
    public static int BELT;
    public static int TRINKET;
    public static int HEAD;
    public static int BODY;
    public static int CHARM;
    public static int maxLevel = 1;

    public Config(FMLPreInitializationEvent event) {
        loadConfig(event);
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ConfigChangeListener.class);
        modDir = event.getModConfigurationDirectory();
        try {
            init(event.getSuggestedConfigurationFile());
        } catch (Exception e) {
            Baubles.log.error("BAUBLES has a problem loading it's configuration");
        } finally {
            if (Config.configFile != null) configFile.save();
        }
    }

    private void init(File file) {
        configFile = new Configuration(file);
        configFile.load();
        init();
    }

    protected void init() {
        renderBaubles = configFile.getBoolean("baubleRender", CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        baublesButton = configFile.getBoolean("baublesButton", "client.gui", baublesButton, "Show baublesButton or not");
        baublesTab = configFile.getBoolean("baublesTab", "client.gui", baublesTab, "Show baublesTab or not");
        babPosX = configFile.getInt("babPosX", "client.gui", babPosX, 0, 255, "The x position of button which calls baublesTab");
        invPosX = configFile.getInt("invPosX", "client.gui", invPosX, 0, 255, "The x position of button which calls inventory");

        maxLevel = configFile.getInt("maxLevel", CATEGORY_GENERAL, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");

        jsonFunction = configFile.getBoolean("jsonFunction", CATEGORY_GENERAL ,jsonFunction, "Activate json function or not.");

        trinketLimit = configFile.getBoolean("trinketLimit", CATEGORY_GENERAL, trinketLimit, "Whether trinketSlot is independent. If true, trinket will become a independent type.");
        keepBaubles = configFile.getBoolean("keepBaubles", CATEGORY_GENERAL, keepBaubles, "Whether baubles can drop when player dies.");

        AMULET = getAmount("amuletSlot", BaubleType.AMULET.getDefaultAmount());
        RING = getAmount("ringSlot", BaubleType.RING.getDefaultAmount());
        BELT = getAmount("beltSlot", BaubleType.BELT.getDefaultAmount());
        TRINKET = getAmount("trinketSlot", BaubleType.TRINKET.getDefaultAmount());
        HEAD = getAmount("headSlot", BaubleType.HEAD.getDefaultAmount());
        BODY = getAmount("bodySlot", BaubleType.BODY.getDefaultAmount());
        CHARM = getAmount("charmSlot", BaubleType.CHARM.getDefaultAmount());

        configFile.save();
    }

    private int getAmount(String key, int value) {
        return configFile.getInt(key, "general.slots", value, 0, 100, "");
    }

    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(Baubles.MODID)) {
                config.init();
                Baubles.baubles = new BaublesContent();
                if (jsonFunction) {
                    Baubles.baubles.writeJson();
                }
            }
        }
    }
}
//todo configFile ui