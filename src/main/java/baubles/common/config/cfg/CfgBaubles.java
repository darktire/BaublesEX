package baubles.common.config.cfg;

import baubles.api.BaubleType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class CfgBaubles extends CfgBase{
    public static int AMULET;
    public static int RING;
    public static int BELT;
    public static int TRINKET;
    public static int HEAD;
    public static int BODY;
    public static int CHARM;
    public final static String BAUBLES_SLOTS = "general.slots";

    public CfgBaubles(Configuration cfgFile) {
        super(cfgFile);
    }

    public void loadData() {
        AMULET = getCfgAmount(cfgFile, "amuletSlot", BaubleType.AMULET.getDefaultAmount());
        RING = getCfgAmount(cfgFile, "ringSlot", BaubleType.RING.getDefaultAmount());// no less than 2 or incompatible with artifact
        BELT = getCfgAmount(cfgFile, "beltSlot", BaubleType.BELT.getDefaultAmount());
        TRINKET = getCfgAmount(cfgFile, "trinketSlot", BaubleType.TRINKET.getDefaultAmount());
        HEAD = getCfgAmount(cfgFile, "headSlot", BaubleType.HEAD.getDefaultAmount());
        BODY = getCfgAmount(cfgFile, "bodySlot", BaubleType.BODY.getDefaultAmount());
        CHARM = getCfgAmount(cfgFile, "charmSlot", BaubleType.CHARM.getDefaultAmount());

        cfgFile.getCategory(BAUBLES_SLOTS).setComment("Modify the quantity of initial baubles.");
    }

    private int getCfgAmount(Configuration cfgFile, String key, int value) {
        return cfgFile.getInt(key, BAUBLES_SLOTS, value, 0, 100, "");
    }

    public static int getCfgAmount(String key){
        Property property = cfgFile.getCategory(BAUBLES_SLOTS).get(key.toLowerCase() + "Slot");
        if (property != null) {
            return property.getInt();
        } else {
            return 0;
        }
    }
}
