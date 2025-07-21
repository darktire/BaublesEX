package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public class CfgGui extends CfgBase{
    public static boolean baublesButton = true;
    public static final String CLIENT_GUI = "client.gui";

    public CfgGui(Configuration cfgFile) {
        super(cfgFile);
    }

    public void loadData(Configuration cfgFile) {
        baublesButton = cfgFile.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baublesButton or not");

        cfgFile.getCategory(CLIENT_GUI).setComment("Edit new gui.");
    }
}
