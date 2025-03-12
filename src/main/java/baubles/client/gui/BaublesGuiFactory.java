package baubles.client.gui;

import baubles.common.Baubles;
import baubles.common.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused") // gets used by Forge annotations
public class BaublesGuiFactory extends DefaultGuiFactory {

    public BaublesGuiFactory() {
        super(Baubles.MODID, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.addAll(new ConfigElement(Config.config
                .getCategory(Configuration.CATEGORY_GENERAL))
                .getChildElements());
        list.addAll(new ConfigElement(Config.config
                .getCategory(Configuration.CATEGORY_CLIENT))
                .getChildElements());

        return list;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new GuiConfig(parent, getConfigElements(), this.modid, false, false, this.title);
    }
}
