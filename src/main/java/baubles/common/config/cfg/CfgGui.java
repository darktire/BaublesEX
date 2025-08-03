package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public class CfgGui extends CfgBase{
    public static boolean baublesButton = true;
    public static boolean scrollerBar = true;
    public static final String CLIENT_GUI = "client.gui";

    public CfgGui(Configuration cfgFile) {
        super(cfgFile);
    }

    public void loadData() {
        baublesButton = cfgFile.getBoolean("baublesButton", CLIENT_GUI, baublesButton, "Show baubles button or not");
        scrollerBar = cfgFile.getBoolean("scrollerBar", CLIENT_GUI, scrollerBar, "Default visibility of the scroller bar ");
        cfgFile.getCategory(CLIENT_GUI).setComment("Edit new gui.");
    }
}
