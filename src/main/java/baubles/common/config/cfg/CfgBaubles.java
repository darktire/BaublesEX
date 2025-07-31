package baubles.common.config.cfg;

import baubles.api.BaubleType;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;

public class CfgBaubles extends CfgBase{
    public static int AMULET;
    public static int RING;
    public static int BELT;
    public static int TRINKET;
    public static int HEAD;
    public static int BODY;
    public static int CHARM;
    private final String BAUBLES_SLOTS = "general.slots";

    public CfgBaubles(Configuration cfgFile) {
        super(cfgFile);
    }

    public void loadData() {
        AMULET = setCfgAmount(cfgFile, "amuletSlot", BaubleType.AMULET.getDefaultAmount());
        RING = setCfgAmount(cfgFile, "ringSlot", BaubleType.RING.getDefaultAmount());// no less than 2 or incompatible with artifact
        BELT = setCfgAmount(cfgFile, "beltSlot", BaubleType.BELT.getDefaultAmount());
        TRINKET = setCfgAmount(cfgFile, "trinketSlot", BaubleType.TRINKET.getDefaultAmount());
        HEAD = setCfgAmount(cfgFile, "headSlot", BaubleType.HEAD.getDefaultAmount());
        BODY = setCfgAmount(cfgFile, "bodySlot", BaubleType.BODY.getDefaultAmount());
        CHARM = setCfgAmount(cfgFile, "charmSlot", BaubleType.CHARM.getDefaultAmount());

        cfgFile.getCategory(BAUBLES_SLOTS).setComment("Modify the quantity of initial baubles.");
    }

    private int setCfgAmount(Configuration cfgFile, String key, int value) {
        return cfgFile.getInt(key, BAUBLES_SLOTS, value, 0, 100, "");
    }

    public static int getCfgAmount(String tag){
        try {
            Class<?> clazz = CfgBaubles.class;
            Field field = clazz.getDeclaredField(tag);
            return field.getInt(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
