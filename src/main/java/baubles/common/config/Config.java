package baubles.common.config;

import baubles.api.BaubleType;
import baubles.common.Baubles;
import baubles.common.BaublesContent;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

import static baubles.common.Baubles.baubles;
import static baubles.common.Baubles.config;

public class Config {
    protected static Configuration configFile;
    private static final Gson GSON = new Gson();
    public static File modDir;
    private static File jsonFile;

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
    private static MoreSetting moreSetting;

    public Config(FMLPreInitializationEvent event) {
        loadConfig(event);
    }

    public void loadConfig(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ConfigChangeListener.class);
        modDir = event.getModConfigurationDirectory();
        jsonFile = new File(modDir, "baubles.json");
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
        renderBaubles = configFile.getBoolean("baubleRender", Configuration.CATEGORY_CLIENT, renderBaubles, "Set this to false to disable rendering of baubles in the player.");

        baublesButton = configFile.getBoolean("baublesButton", "client.gui", baublesButton, "Show baublesButton or not");
        baublesTab = configFile.getBoolean("baublesTab", "client.gui", baublesTab, "Show baublesTab or not");
        babPosX = configFile.getInt("babPosX", "client.gui", babPosX, 0, 255, "The x position of button which calls baublesTab");
        invPosX = configFile.getInt("invPosX", "client.gui", invPosX, 0, 255, "The x position of button which calls inventory");

//        mode = configFile.getString("mode", Configuration.CATEGORY_GENERAL ,mode, "NORMAL mode is current mode with all functions. \nOLD mode is back to classic style of baubles and support only 7 slots.", validMode, validMode);

//        trinketLimit = configFile.getBoolean("trinketLimit", Configuration.CATEGORY_GENERAL, trinketLimit, "(Invalid)Whether trinketSlot is controlled independently. If false, value of trinketSlot won't work.");

        AMULET = getAmount("amuletSlot", BaubleType.AMULET.getDefaultAmount());
        RING = getAmount("ringSlot", BaubleType.RING.getDefaultAmount());
        BELT = getAmount("beltSlot", BaubleType.BELT.getDefaultAmount());
        TRINKET = getAmount("trinketSlot", BaubleType.TRINKET.getDefaultAmount());
        HEAD = getAmount("headSlot", BaubleType.HEAD.getDefaultAmount());
        BODY = getAmount("bodySlot", BaubleType.BODY.getDefaultAmount());
        CHARM = getAmount("charmSlot", BaubleType.CHARM.getDefaultAmount());

//        try {
//            if (jsonFile.exists()) {
//                    moreSetting = GSON.fromJson(new FileReader(jsonFile), MoreSetting.class);
//            }
//            else {
//                    Files.createFile(jsonFile.toPath());
//                    moreSetting = new MoreSetting("amulet", BaubleType.AMULET.getDefaultAmount());
//                    FileUtils.write(jsonFile, GSON.toJson(moreSetting), StandardCharsets.UTF_8);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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
                baubles = new BaublesContent();
            }
        }
    }

    public static class MoreSetting {
        @Expose
        private String type;
        @Expose
        private int amount;
        public MoreSetting(String type, int amount) {
            this.type = type;
            this.amount = amount;
        }
    }
}
//todo configFile ui