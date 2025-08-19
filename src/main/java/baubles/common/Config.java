package baubles.common;

import baubles.Baubles;
import baubles.api.BaubleType;
import baubles.api.cap.BaublesContainer;
import baubles.common.config.json.JsonHelper;
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
    public static JsonHelper json;
    public File modDir;

//    Configuration Options
    public static boolean renderBaubles = true;
    public static boolean keepBaubles = false;
    public static int maxLevel = 1;
//    public static boolean armorStand = false;
    private static String[] clickBlacklist = {"wct:wct"};

    public final static String BAUBLES_SLOTS = "general.slots";
    public final static String CLIENT_GUI = "client.gui";
    public final static String BAUBLES_ITEMS = "general.items";

    private final LinkedList<Item> blacklist = new LinkedList<>();

    public Config(FMLPreInitializationEvent event) {
        loadConfig(event);
        MinecraftForge.EVENT_BUS.register(Config.ConfigChangeListener.class);
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        this.modDir = event.getModConfigurationDirectory();
        try {
            configFile = new Configuration(event.getSuggestedConfigurationFile());
            configFile.load();

            new Slots();
            new Gui();
            new ModItems();
            loadData();
            json = new JsonHelper(this.modDir);
        } catch (Exception e) {
            Baubles.log.error("BAUBLES has a problem loading it's configuration");
        } finally {
            if (Config.configFile != null) configFile.save();
        }
    }


    public void loadData() {
        renderBaubles = configFile.getBoolean("baubleRender", CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        maxLevel = configFile.getInt("maxLevel", CATEGORY_GENERAL, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");

        keepBaubles = configFile.getBoolean("keepBaubles", CATEGORY_GENERAL, keepBaubles, "Whether baubles can drop when player dies.");
//        armorStand = configFile.getBoolean("armorStand", CATEGORY_GENERAL, armorStand, "Whether armorStand has baubles container (need to place armorStand again)");

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
            AMULET = getCfgAmount("amuletSlot", BaubleType.AMULET.amount);
            RING = getCfgAmount("ringSlot", BaubleType.RING.amount);// no less than 2 or incompatible with artifact
            BELT = getCfgAmount("beltSlot", BaubleType.BELT.amount);
            TRINKET = getCfgAmount("trinketSlot", BaubleType.TRINKET.amount, "Number of slots only for trinket is equal to the difference between the value you set and all other slots.");
            HEAD = getCfgAmount("headSlot", BaubleType.HEAD.amount);
            BODY = getCfgAmount("bodySlot", BaubleType.BODY.amount);
            CHARM = getCfgAmount("charmSlot", BaubleType.CHARM.amount);

            configFile.getCategory(BAUBLES_SLOTS).setComment("Modify the quantity of initial baubles.");
        }

        private int getCfgAmount(String key, int value) {
            return getCfgAmount(key , value,"");
        }

        private int getCfgAmount(String key, int value, String comment) {
            return configFile.getInt(key, BAUBLES_SLOTS, value, 0, 100, comment);
        }

        public static int getCfgAmount(String key){
            Property property = configFile.getCategory(BAUBLES_SLOTS).get(key.toLowerCase() + "Slot");
            if (property != null) return property.getInt();
            else return 0;
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
            testItem = configFile.getBoolean("testItem", BAUBLES_ITEMS, testItem, "For test, or you want");
            elytraBauble = configFile.getBoolean("elytraBauble", BAUBLES_ITEMS, elytraBauble, "Set elytra as bauble");
            elytraSlot = configFile.getString("elytraSlot", BAUBLES_ITEMS, elytraSlot, "Get a specific slot for elytra", elytraValidSlot);
            configFile.getCategory(BAUBLES_ITEMS).setComment("Item modified by BaublesEX. (need to restart)");
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
                for (BaublesContainer container: BaublesContainer.listener) {
                    container.onConfigChanged();
                }
            }
        }
    }
}
//exposed as old api