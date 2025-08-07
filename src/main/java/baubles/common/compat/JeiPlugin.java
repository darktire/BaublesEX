package baubles.common.compat;

import baubles.client.gui.GuiPlayerExpanded;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    public static final JeiCompatProvider JEI_COMPAT = new JeiCompatProvider();

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(JEI_COMPAT);
    }

    public static final class JeiCompatProvider implements IAdvancedGuiHandler<GuiPlayerExpanded> {

        @Override
        public Class<GuiPlayerExpanded> getGuiContainerClass() {
            return GuiPlayerExpanded.class;
        }

        @Override
        public List<Rectangle> getGuiExtraAreas(GuiPlayerExpanded gui) {
            return gui.getExtraArea();
        }

        @Override
        public Integer getIngredientUnderMouse(GuiPlayerExpanded guiContainer, int mouseX, int mouseY) {
            return Mouse.getDWheel();
        }
    }
}
