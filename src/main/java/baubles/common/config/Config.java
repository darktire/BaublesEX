package baubles.common.config;

import baubles.BaublesRegister;
import baubles.api.BaublesApi;
import baubles.api.attribute.AttributeManager;
import baubles.common.config.json.Category;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static String[] itemsJson;
    private static String[] typesJson;
    private static String[] modulesJson;

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

        clickBlacklist = config.getStringList("clickBlacklist", CATEGORY_GENERAL, new String[]{"wct:wct"}, "");
        itemsJson = config.getStringList("itemsJson", CATEGORY_GENERAL, new String[]{"items.json"}, "");
        typesJson = config.getStringList("typesJson", CATEGORY_GENERAL, new String[]{"types.json"}, "");
        modulesJson = config.getStringList("modulesJson", CATEGORY_GENERAL, new String[]{}, "");

        PartialConfig.create(Slots.class, Gui.class, ModItems.class, Commands.class);
    }

    public static List<File> getJson(Category category) {
        String[] names = new String[0];
        if (category == Category.ITEM_DATA) names = itemsJson;
        if (category == Category.TYPE_DATA) names = typesJson;
        if (category == Category.MODULE_DATA) names = modulesJson;
        List<File> files = new ArrayList<>();
        for (String name : names) {
            FileFilter filter = new WildcardFileFilter(name);
            File[] matched = getModDir().listFiles(filter);
            if (matched != null && matched.length > 0) {
                files.addAll(Arrays.asList(matched));
            }
        }
        return files;
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
        public static boolean typeTip;
        public static boolean overlay;
        private static String[] guiList;
        private static final String[] defList = {"vazkii.botania.client.gui.box.GuiBaubleBox", "cursedflames.bountifulbaubles.block.GuiReforger"};
        private static final List<Class<?>> guis = new ArrayList<>();
        public static int column;

        @Override
        public void loadData() {
            baublesButton = config.getBoolean("baublesButton", CLIENT_GUI, true, "Show baubles button or not");
            scrollerBar = config.getBoolean("scrollerBar", CLIENT_GUI, true, "Default visibility of the scroller bar");
            widerBar = config.getBoolean("widerBar", CLIENT_GUI, false, "Default selection of the sidebar");
            column = config.getInt("column", CLIENT_GUI, 2, 2,5, "Columns of the wider sidebar");
            visibleSwitchers = config.getBoolean("visibleSwitchers", CLIENT_GUI, true, "Show visible switchers or not");
            typeTip = config.getBoolean("typeTip", CLIENT_GUI, true, "Show the tip for baubles' type or not");
            overlay = config.getBoolean("guiOverlay", CLIENT_GUI, true, "Patch some containers such as BaubleBox. If false, it will patch after the selected key pressed");
            guiList = config.getStringList("guiList", CLIENT_GUI, defList, "Gui needed baubles overlay");
            config.getCategory(CLIENT_GUI).setComment("Edit new gui.");

            try {
                initCls();
            } catch (Exception ignored) {}
        }

        private void initCls() throws ClassNotFoundException {
            for (String s : guiList) {
                guis.add(Class.forName(s, false, this.getClass().getClassLoader()));
            }
        }

        public static boolean isTarget(Object o) {
            return guis.stream().anyMatch(cls -> cls.isInstance(o));
        }
    }

    public static class ModItems extends PartialConfig {
        public static boolean testItem;
        public static boolean itemRing;
        public static int maxLevel;
        public static boolean elytraBauble;
        public static String elytraSlot;
        public static boolean totemBauble;
        public static String totemSlot;
        private final String[] presetSlots = {"amulet", "ring", "belt", "trinket", "head", "body", "charm", "elytra"};

        @Override
        public void loadData() {
            testItem = config.getBoolean("testItem", BAUBLES_ITEMS, false, "For test, or you want");
            itemRing = config.getBoolean("itemRing", BAUBLES_ITEMS, true, "The ring added by original Baubles");
            maxLevel = config.getInt("maxLevel", BAUBLES_ITEMS, 1, 0, 255, "Max level of haste given by Miner's Ring");
            elytraBauble = config.getBoolean("elytraBauble", BAUBLES_ITEMS, false, "Set elytra as bauble");
            elytraSlot = config.getString("elytraSlot", BAUBLES_ITEMS, "elytra", "Get a specific slot for elytra", presetSlots);
            totemBauble = config.getBoolean("totemBauble", BAUBLES_ITEMS, false, "Set totem of undying as bauble");
            totemSlot = config.getString("totemSlot", BAUBLES_ITEMS, "charm", "Get a specific slot for elytra", presetSlots);
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
        AttributeManager.loadAttributes();
    }
}