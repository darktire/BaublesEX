package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public class CfgGui extends CfgBase{
    public static boolean baublesButton = true;
    public static boolean baublesTab = false;
    public static int invPosX = 0;
    public static int babPosX = 28;
    public static final String CLIENT_GUI = "client.gui";

    public CfgGui(Configuration cfgFile) {
        super(cfgFile);
    }

    public void loadData(Configuration cfgFile) {
        baublesButton = cfgFile.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baublesButton or not");
        baublesTab = cfgFile.getBoolean("baublesTab", CLIENT_GUI, baublesTab, "Show baublesTab or not");
        babPosX = cfgFile.getInt("babPosX", CLIENT_GUI, babPosX, 0, 255, "The x position of button which calls baublesTab");
        invPosX = cfgFile.getInt("invPosX", CLIENT_GUI, invPosX, 0, 255, "The x position of button which calls inventory");

        cfgFile.getCategory(CLIENT_GUI).setComment("Edit new gui.");
    }
}
