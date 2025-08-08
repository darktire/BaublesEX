package baubles.common;

import baubles.api.BaubleType;
import baubles.api.registries.TypesData;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.LinkedList;

import static net.minecraftforge.common.config.Configuration.CATEGORY_CLIENT;
import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config {
    private static Configuration configFile;
    public File modDir;

//    Configuration Options
    public static boolean renderBaubles = true;
//    public static boolean jsonFunction = false;
    public static boolean keepBaubles = false;
    public static int maxLevel = 1;
    protected static String[] clickBlacklist = {"wct:wct"};
    public final static String BAUBLES_SLOTS = "general.slots";
    public final static String CLIENT_GUI = "client.gui";
    public final static String BAUBLES_ITEMS = "general.items";

    private final LinkedList<Item> blacklist = new LinkedList<>();

    public Config(FMLPreInitializationEvent event) {
        loadConfig(event);
        MinecraftForge.EVENT_BUS.register(Config.ConfigChangeListener.class);
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        modDir = event.getModConfigurationDirectory();
        try {
            configFile = new Configuration(event.getSuggestedConfigurationFile());
            configFile.load();

            new Slots();
            new Gui();
            new ModItems();
            loadData();
        } catch (Exception e) {
            Baubles.log.error("BAUBLES has a problem loading it's configuration");
        } finally {
            if (Config.configFile != null) configFile.save();
        }
    }


    public void loadData() {
        renderBaubles = configFile.getBoolean("baubleRender", CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        maxLevel = configFile.getInt("maxLevel", CATEGORY_GENERAL, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");

//        jsonFunction = configFile.getBoolean("jsonFunction", CATEGORY_GENERAL ,jsonFunction, "Activate json function or not. (experimental function)");

        keepBaubles = configFile.getBoolean("keepBaubles", CATEGORY_GENERAL, keepBaubles, "Whether baubles can drop when player dies.");

        clickBlacklist = configFile.getStringList("clickBlacklist", CATEGORY_GENERAL, clickBlacklist, "");

        configFile.save();
    }

    public Configuration getConfigFile() {
        return configFile;
    }

    public LinkedList<Item> getBlacklist() {
        return this.blacklist;
    }

    public void setupBlacklist() {
        for (String s : clickBlacklist) {
            Item item = Item.getByNameOrId(s);
            if (item != null) blacklist.add(item);
        }
    }

    public abstract static class Base {
        public Base(){ loadData(); }
        public abstract void loadData();
    }

    public static class Slots extends Base {
        public static int AMULET;
        public static int RING;
        public static int BELT;
        public static int TRINKET;
        public static int HEAD;
        public static int BODY;
        public static int CHARM;

        @Override
        public void loadData() {
            AMULET = getCfgAmount(configFile, "amuletSlot", BaubleType.AMULET.amount);
            RING = getCfgAmount(configFile, "ringSlot", BaubleType.RING.amount);// no less than 2 or incompatible with artifact
            BELT = getCfgAmount(configFile, "beltSlot", BaubleType.BELT.amount);
            TRINKET = getCfgAmount(configFile, "trinketSlot", BaubleType.TRINKET.amount);
            HEAD = getCfgAmount(configFile, "headSlot", BaubleType.HEAD.amount);
            BODY = getCfgAmount(configFile, "bodySlot", BaubleType.BODY.amount);
            CHARM = getCfgAmount(configFile, "charmSlot", BaubleType.CHARM.amount);

            configFile.getCategory(BAUBLES_SLOTS).setComment("Modify the quantity of initial baubles.");
        }

        private int getCfgAmount(Configuration cfgFile, String key, int value) {
            return cfgFile.getInt(key, BAUBLES_SLOTS, value, 0, 100, "");
        }

        public static int getCfgAmount(String key){
            Property property = configFile.getCategory(BAUBLES_SLOTS).get(key.toLowerCase() + "Slot");
            if (property != null) {
                return property.getInt();
            } else {
                return 0;
            }
        }
    }

    public static class Gui extends Base {
        public static boolean baublesButton = true;
        public static boolean scrollerBar = true;
        public static boolean widerBar = false;
        public static int column = 2;

        @Override
        public void loadData() {
            baublesButton = configFile.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baubles button or not");
            scrollerBar = configFile.getBoolean("scrollerBar", CLIENT_GUI, scrollerBar, "Default visibility of the scroller bar");
            widerBar = configFile.getBoolean("widerBar", CLIENT_GUI, widerBar, "Default selection of the sidebar");
            column = configFile.getInt("column", CLIENT_GUI, column, 2,5, "Columns of the wider sidebar");
            configFile.getCategory(CLIENT_GUI).setComment("Edit new gui.");
        }
    }

    public static class ModItems extends Base {
        public static boolean testItem = false;
        public static boolean elytraBauble = false;
        public static String elytraSlot = "elytra";
        private final String[] elytraValidSlot = {"amulet", "ring", "belt", "trinket", "head", "body", "charm", "elytra"};

        @Override
        public void loadData() {
            testItem = configFile.getBoolean("testItem", BAUBLES_ITEMS, testItem, "");
            elytraBauble = configFile.getBoolean("elytraBauble", BAUBLES_ITEMS, elytraBauble, "");
            elytraSlot = configFile.getString("elytraSlot", BAUBLES_ITEMS, elytraSlot, "", elytraValidSlot);
            configFile.getCategory(BAUBLES_ITEMS).requiresMcRestart();
        }
    }


    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(Baubles.MODID)) {
                new Slots();
                new Gui();
                new ModItems();
                Baubles.config.loadData();
                Baubles.config.setupBlacklist();
                Baubles.registry.registerBaubles();
                Baubles.registry.loadValidSlots();
                TypesData.changed = true;
            }
        }
    }
}
//exposed as old api