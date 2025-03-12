package baubles.common;

import baubles.api.BaubleType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class Config {

    public static Configuration config;
    public static File modDir;

//    Configuration Options
    public static boolean renderBaubles = true;
    public static boolean baublesButton = true;
    public static boolean baublesTab = false;
    public static int invPosX = 0;
    public static int babPosX = 28;
    public static String mode = "NORMAL";
//    public static String[] validMode = {"NORMAL", "OLD"};
    public static boolean trinketLimit = false;
    public static int AMULET;
    public static int RING;
    public static int BELT;
    public static int TRINKET;
    public static int HEAD;
    public static int BODY;
    public static int CHARM;
    public static int maxLevel = 1;
    public static String[] newSlot = {};

    private static void initConfig(File file) {
        config = new Configuration(file);
        config.load();
        loadConfigs();
    }

    public static void configLoader(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ConfigChangeListener.class);
        modDir = event.getModConfigurationDirectory();
        try {
            initConfig(event.getSuggestedConfigurationFile());
        } catch (Exception e) {
            Baubles.log.error("BAUBLES has a problem loading it's configuration");
        } finally {
            if (Config.config != null) config.save();
        }
    }

    public static void loadConfigs() {
        renderBaubles = config.getBoolean("baubleRender", Configuration.CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        baublesButton = config.getBoolean("baublesButton", "client.gui", baublesButton, "Show baublesButton or not");
        baublesTab = config.getBoolean("baublesTab", "client.gui", baublesTab, "Show baublesTab or not");
        babPosX = config.getInt("babPosX", "client.gui", babPosX, 0, 255, "The x position of button which calls baublesTab");
        invPosX = config.getInt("invPosX", "client.gui", invPosX, 0, 255, "The x position of button which calls inventory");

//        mode = config.getString("mode", Configuration.CATEGORY_GENERAL ,mode, "NORMAL mode is current mode with all functions. \nOLD mode is back to classic style of baubles and support only 7 slots.", validMode, validMode);

//        trinketLimit = config.getBoolean("trinketLimit", Configuration.CATEGORY_GENERAL, trinketLimit, "(Invalid)Whether trinketSlot is controlled independently. If false, value of trinketSlot won't work.");

        AMULET = defaultSlots("amuletSlot", BaubleType.AMULET.amount);
        RING = defaultSlots("ringSlot", BaubleType.RING.amount);
        BELT = defaultSlots("beltSlot",BaubleType.BELT.amount);
//        TRINKET = defaultSlots("trinketSlot",BaubleType.TRINKET.amount);
        HEAD = defaultSlots("headSlot",BaubleType.HEAD.amount);
        BODY = defaultSlots("bodySlot",BaubleType.BODY.amount);
        CHARM = defaultSlots("charmSlot",BaubleType.CHARM.amount);

        config.save();
        BaubleContent.initSlots();
    }

    private static int defaultSlots(String key, int value) {
        return config.getInt(key, "general.slots", value, 0, 100, "");
    }

    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(Baubles.MODID)) loadConfigs();
        }
    }
}
//todo config ui