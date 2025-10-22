package baubles.common.config;

import baubles.BaublesRegister;
import baubles.api.BaublesApi;
import baubles.api.cap.BaublesContainer;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config extends PartialConfig {
    private static final File CONFIG_DIR;
    private static final Configuration config;

    static {
        CONFIG_DIR = new File(Launch.minecraftHome, "config");
        config = new Configuration(new File(CONFIG_DIR, BaublesApi.MOD_ID + ".cfg"));
        config.load();
    }

    private static final File MOD_DIR = new File(CONFIG_DIR, BaublesApi.MOD_ID);

    public static boolean keepBaubles;
    public static boolean rightClick;
//    public static boolean armorStand = false;
    private static String[] clickBlacklist;
    private static final String[] defList = {"wct:wct"};

    public final static String BAUBLES_SLOTS = "general.slots";
    public final static String CLIENT_GUI = "client.gui";
    public final static String BAUBLES_ITEMS = "general.items";
    public final static String BAUBLES_COMMANDS = "general.commands";

    private static final List<Item> blacklist = new ArrayList<>();

    public static void loadConfig() {
        try {
            PartialConfig.create(Config.class);
        } catch (Exception e) {
            BaublesApi.log.error("BAUBLES has a problem loading it's configuration");
        }
        checkConfig(config);
        saveConfig();
    }

    public void loadData() {
        keepBaubles = config.getBoolean("keepBaubles", CATEGORY_GENERAL, false, "Whether baubles can drop when player dies.");
        rightClick = config.getBoolean("rightClick", CATEGORY_GENERAL, true, "Whether player can use right click to equip baubles.");
//        armorStand = config.getBoolean("armorStand", CATEGORY_GENERAL, armorStand, "Whether armorStand has baubles container (need to place armorStand again)");

        clickBlacklist = config.getStringList("clickBlacklist", CATEGORY_GENERAL, defList, "");

        PartialConfig.create(Slots.class, Gui.class, ModItems.class, Commands.class);
    }

    private static void checkConfig(Configuration cfg) {
        for (String catName : cfg.getCategoryNames()) {
            ConfigCategory cat = cfg.getCategory(catName);
            checkCategory(cat, "");
        }
    }

    private static void checkCategory(ConfigCategory cat, String path) {
        for (Map.Entry<String, Property> entry : cat.entrySet()) {
            if (entry.getValue().getComment().isEmpty()) {
                cat.remove(entry.getKey());
            }
        }
        for (ConfigCategory child : cat.getChildren()) {
            checkCategory(child, path.isEmpty() ? cat.getName() : path + "." + cat.getName());
        }
    }

    public static void saveConfig() {
        if (config != null && config.hasChanged()) {
            config.save();
        }
    }

    public static Configuration getInstance() {
        return config;
    }

    public static File getModDir() {
        return MOD_DIR;
    }

    public static List<Item> getBlacklist() {
        return blacklist;
    }

    public static void setupBlacklist() {
        if (rightClick)  {
            for (String s : clickBlacklist) {
                Item item = Item.getByNameOrId(s);
                if (item != null) blacklist.add(item);
            }
        }
    }

    public static class Slots extends PartialConfig {
        public static int AMULET;
        public static int RING;
        public static int BELT;
        public static int TRINKET;
        public static int HEAD;
        public static int BODY;
        public static int CHARM;

        @Override
        public void loadData() {
            AMULET = getCfgAmount("amuletSlot", 1);
            RING = getCfgAmount("ringSlot", 2);
            BELT = getCfgAmount("beltSlot", 1);
            TRINKET = getCfgAmount("trinketSlot", 0);
            HEAD = getCfgAmount("headSlot", 1);
            BODY = getCfgAmount("bodySlot", 1);
            CHARM = getCfgAmount("charmSlot", 1);

            getCategory().setComment("Modify the quantity of initial baubles.");
        }

        private int getCfgAmount(String key, int value) {
            return config.getInt(key, BAUBLES_SLOTS, value, 0, 100, "Set slots for " + key.replace("Slot", ""));
        }

        public static ConfigCategory getCategory() {
            return config.getCategory(BAUBLES_SLOTS);
        }

        public static int getCfgAmount(String key){
            Property property = getCategory().get(key.toLowerCase() + "Slot");
            if (property != null) return property.getInt();
            else return 0;
        }
    }

    public static class Gui extends PartialConfig {
        public static boolean baublesButton;
        public static boolean scrollerBar;
        public static boolean widerBar;
        public static boolean visibleSwitchers;
        public static int column;

        @Override
        public void loadData() {
            baublesButton = config.getBoolean("baublesButton", CLIENT_GUI, true, "Show baubles button or not");
            scrollerBar = config.getBoolean("scrollerBar", CLIENT_GUI, true, "Default visibility of the scroller bar");
            widerBar = config.getBoolean("widerBar", CLIENT_GUI, false, "Default selection of the sidebar");
            column = config.getInt("column", CLIENT_GUI, 2, 2,5, "Columns of the wider sidebar");
            visibleSwitchers = config.getBoolean("visibleSwitchers", CLIENT_GUI, true, "Show visible switchers or not");
            config.getCategory(CLIENT_GUI).setComment("Edit new gui.");
        }
    }

    public static class ModItems extends PartialConfig {
        public static boolean testItem;
        public static boolean itemRing;
        public static int maxLevel;
        public static boolean elytraBauble;
        public static String elytraSlot;
        private final String[] elytraValidSlot = {"amulet", "ring", "belt", "trinket", "head", "body", "charm", "elytra"};

        @Override
        public void loadData() {
            testItem = config.getBoolean("testItem", BAUBLES_ITEMS, false, "For test, or you want");
            itemRing = config.getBoolean("itemRing", BAUBLES_ITEMS, true, "The ring added by original Baubles");
            maxLevel = config.getInt("maxLevel", BAUBLES_ITEMS, 1, 0, 255, "Max level of haste given by Miner's Ring");
            elytraBauble = config.getBoolean("elytraBauble", BAUBLES_ITEMS, false, "Set elytra as bauble");
            elytraSlot = config.getString("elytraSlot", BAUBLES_ITEMS, "elytra", "Get a specific slot for elytra", elytraValidSlot);
            ConfigCategory category = config.getCategory(BAUBLES_ITEMS);
            category.setComment("Item modified by BaublesEX. (need to restart)");
            category.requiresMcRestart();
        }
    }

    public static class Commands extends PartialConfig {
        public static boolean debug;
        public static boolean commandLogs;
        public static boolean dropBaubles;

        @Override
        public void loadData() {
            debug = config.getBoolean("debug", BAUBLES_COMMANDS, false, "Make /baubles debug commands available for use");
            commandLogs = config.getBoolean("commandLogs", BAUBLES_COMMANDS, true, "Whether /baubles commands send logs when commands have executed successfully");
            dropBaubles = config.getBoolean("dropBaubles", BAUBLES_COMMANDS, true, "Whether /baubles clear will drop baubles");
        }
    }

    @Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(BaublesApi.MOD_ID)) {
                PartialConfig.create(Config.class);
                Config.saveConfig();
                Config.setupBlacklist();
                syncToBaubles();
            }
        }
    }

    public static void syncToBaubles() {
        BaublesRegister.setTypes();
        BaublesRegister.loadValidSlots();
        for (BaublesContainer container: BaublesContainer.CONTAINERS) {
            container.onConfigChanged();
        }
    }
}