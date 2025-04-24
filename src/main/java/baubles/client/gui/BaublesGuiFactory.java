package baubles.client.gui;

import baubles.common.Baubles;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

import static baubles.common.Baubles.config;

@SuppressWarnings("unused") // gets used by Forge annotations
public class BaublesGuiFactory extends DefaultGuiFactory {

    public BaublesGuiFactory() {
        super(Baubles.MODID, GuiConfig.getAbridgedConfigPath(config.getConfigFile().toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.addAll(new ConfigElement(config.getConfigFile().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(config.getConfigFile().getCategory(Configuration.CATEGORY_CLIENT)).getChildElements());

        return list;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new GuiConfig(parent, getConfigElements(), this.modid, false, false, this.title);
    }
}
