package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public abstract class CfgBase {
    protected static Configuration cfgFile;

    public CfgBase(Configuration file){
        cfgFile = file;
        if (cfgFile != null) loadData();
    }

    public abstract void loadData();
}
