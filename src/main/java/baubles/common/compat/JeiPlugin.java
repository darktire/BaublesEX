package baubles.common.compat;

import baubles.client.gui.GuiPlayerExpanded;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import java.awt.*;
import java.util.List;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(new BaublesAreaProvider());
    }

    public static final class BaublesAreaProvider implements IAdvancedGuiHandler<GuiPlayerExpanded> {

        @Override
        public Class<GuiPlayerExpanded> getGuiContainerClass() {
            return GuiPlayerExpanded.class;
        }

        @Override
        public List<Rectangle> getGuiExtraAreas(GuiPlayerExpanded gui) {
            return gui.getExtraArea();
        }
    }
}
