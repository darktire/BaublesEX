package baubles.common.config.cfg;

import net.minecraftforge.common.config.Configuration;

public abstract class CfgBase {
    protected final Configuration cfgFile;

    public CfgBase(Configuration cfgFile){
        this.cfgFile = cfgFile;
        loadData();
    }

    public abstract void loadData();
}
