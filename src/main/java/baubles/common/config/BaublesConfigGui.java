package baubles.common.config;

import baubles.common.Baubles;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

import static baubles.common.Baubles.config;

public class BaublesConfigGui extends GuiConfig {

    public BaublesConfigGui(GuiScreen parent) {
        super(getParent(parent), getConfigElements(), Baubles.MODID, false, false, getTitle(parent));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();

        list.addAll(new ConfigElement(config.getConfigFile().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(config.getConfigFile().getCategory(Configuration.CATEGORY_CLIENT)).getChildElements());

        return list;
    }

    private static GuiScreen getParent(GuiScreen parent) {
        return parent;
    }

    private static String getTitle(GuiScreen parent) {
        return GuiConfig.getAbridgedConfigPath(config.getConfigFile().toString());
    }
}
