package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public abstract class CfgBase {
    public CfgBase(Configuration cfgFile){
        if(cfgFile != null) loadData(cfgFile);
    }

    public abstract void loadData(Configuration cfgFile);
}
