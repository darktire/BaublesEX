package baubles.common.config;

import baubles.BaublesRegister;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.BaublesContainer;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class Config extends PartialConfig {
    private static Configuration configIns;
    public static File MOD_DIR;

    //    Configuration Options
    public static boolean keepBaubles = false;
    public static boolean rightClick = true;
//    public static boolean armorStand = false;
    private static String[] clickBlacklist = {"wct:wct"};

    public final static String BAUBLES_SLOTS = "general.slots";
    public final static String CLIENT_GUI = "client.gui";
    public final static String BAUBLES_ITEMS = "general.items";
    public final static String BAUBLES_COMMANDS = "general.commands";

    private static final LinkedList<Item> blacklist = new LinkedList<>();

    public static void loadConfig(FMLPreInitializationEvent event) {
        MOD_DIR =new File(event.getModConfigurationDirectory(), BaublesApi.MOD_ID);
        try {
            configIns = new Configuration(event.getSuggestedConfigurationFile());
            configIns.load();

            PartialConfig.create(Config.class);
        } catch (Exception e) {
            BaublesApi.log.error("BAUBLES has a problem loading it's configuration");
        }
        checkConfig(configIns);
        saveConfig();
    }

    public void loadData() {
        keepBaubles = configIns.getBoolean("keepBaubles", CATEGORY_GENERAL, keepBaubles, "Whether baubles can drop when player dies.");
        rightClick = configIns.getBoolean("rightClick", CATEGORY_GENERAL, rightClick, "Whether player can use right click to equip baubles.");
//        armorStand = configIns.getBoolean("armorStand", CATEGORY_GENERAL, armorStand, "Whether armorStand has baubles container (need to place armorStand again)");

        clickBlacklist = configIns.getStringList("clickBlacklist", CATEGORY_GENERAL, clickBlacklist, "");

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
        if (configIns != null && configIns.hasChanged()) {
            configIns.save();
        }
    }

    public static Configuration getConfigIns() {
        return configIns;
    }

    public static LinkedList<Item> getBlacklist() {
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
            AMULET = getCfgAmount("amuletSlot", BaubleType.AMULET.amount);
            RING = getCfgAmount("ringSlot", BaubleType.RING.amount);
            BELT = getCfgAmount("beltSlot", BaubleType.BELT.amount);
            TRINKET = getCfgAmount("trinketSlot", BaubleType.TRINKET.amount);
            HEAD = getCfgAmount("headSlot", BaubleType.HEAD.amount);
            BODY = getCfgAmount("bodySlot", BaubleType.BODY.amount);
            CHARM = getCfgAmount("charmSlot", BaubleType.CHARM.amount);

            getCategory().setComment("Modify the quantity of initial baubles.");
        }

        private int getCfgAmount(String key, int value) {
            return configIns.getInt(key, BAUBLES_SLOTS, value, 0, 100, "Set slots for " + key.replace("Slot", ""));
        }

        public static ConfigCategory getCategory() {
            return configIns.getCategory(BAUBLES_SLOTS);
        }

        public static int getCfgAmount(String key){
            Property property = getCategory().get(key.toLowerCase() + "Slot");
            if (property != null) return property.getInt();
            else return 0;
        }
    }

    public static class Gui extends PartialConfig {
        public static boolean baublesButton = true;
        public static boolean scrollerBar = true;
        public static boolean widerBar = false;
        public static boolean visibleSwitchers = true;
        public static int column = 2;
        public static boolean aetherButton = false;

        @Override
        public void loadData() {
            baublesButton = configIns.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baubles button or not");
            scrollerBar = configIns.getBoolean("scrollerBar", CLIENT_GUI, scrollerBar, "Default visibility of the scroller bar");
            widerBar = configIns.getBoolean("widerBar", CLIENT_GUI, widerBar, "Default selection of the sidebar");
            column = configIns.getInt("column", CLIENT_GUI, column, 2,5, "Columns of the wider sidebar");
            visibleSwitchers = configIns.getBoolean("visibleSwitchers", CLIENT_GUI, visibleSwitchers, "Show visible switchers or not");
            aetherButton = configIns.getBoolean("aetherButton", CLIENT_GUI, aetherButton, "Show aether accessory button or not");
            configIns.getCategory(CLIENT_GUI).setComment("Edit new gui.");
        }
    }

    public static class ModItems extends PartialConfig {
        public static boolean testItem = false;
        public static boolean itemRing = true;
        public static int maxLevel = 1;
        public static boolean elytraBauble = false;
        public static String elytraSlot = "elytra";
        private final String[] elytraValidSlot = {"amulet", "ring", "belt", "trinket", "head", "body", "charm", "elytra"};

        @Override
        public void loadData() {
            testItem = configIns.getBoolean("testItem", BAUBLES_ITEMS, testItem, "For test, or you want");
            itemRing = configIns.getBoolean("itemRing", BAUBLES_ITEMS, itemRing, "The ring added by original Baubles");
            maxLevel = configIns.getInt("maxLevel", BAUBLES_ITEMS, maxLevel, 0, 255, "Max level of haste given by Miner's Ring");
            elytraBauble = configIns.getBoolean("elytraBauble", BAUBLES_ITEMS, elytraBauble, "Set elytra as bauble");
            elytraSlot = configIns.getString("elytraSlot", BAUBLES_ITEMS, elytraSlot, "Get a specific slot for elytra", elytraValidSlot);
            ConfigCategory category = configIns.getCategory(BAUBLES_ITEMS);
            category.setComment("Item modified by BaublesEX. (need to restart)");
            category.requiresMcRestart();
        }
    }

    public static class Commands extends PartialConfig {
        public static boolean debug = false;
        public static boolean commandLogs = true;
        public static boolean dropBaubles = true;

        @Override
        public void loadData() {
            debug = configIns.getBoolean("debug", BAUBLES_COMMANDS, debug, "Make /baubles debug commands available for use");
            commandLogs = configIns.getBoolean("commandLogs", BAUBLES_COMMANDS, commandLogs, "Whether /baubles commands send logs when commands have executed successfully");
            dropBaubles = configIns.getBoolean("dropBaubles", BAUBLES_COMMANDS, dropBaubles, "Whether /baubles clear will drop baubles");
        }
    }

    @Mod.EventBusSubscriber(modid = BaublesApi.MOD_ID)
    public static class ConfigChangeListener {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
            if (eventArgs.getModID().equals(BaublesApi.MOD_ID)) {
                PartialConfig.create(Config.class);
                Config.saveConfig();
                if (Config.rightClick) Config.setupBlacklist();
                syncToBaubles();
            }
        }
    }

    public static void syncToBaubles() {
        BaublesRegister.registerTypes();
        BaublesRegister.loadValidSlots();
        for (BaublesContainer container: BaublesContainer.CONTAINERS) {
            container.onConfigChanged();
        }
    }
}