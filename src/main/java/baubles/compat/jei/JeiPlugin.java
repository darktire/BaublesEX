package baubles.compat.jei;

import baubles.client.gui.event.GuiOverlayHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    public static final JeiCompatProvider JEI_COMPAT = new JeiCompatProvider();

    @Override
    public void register(IModRegistry registry) {
        registry.addAdvancedGuiHandlers(JEI_COMPAT);
    }

    public static final class JeiCompatProvider implements IAdvancedGuiHandler<GuiContainer> {

        @Override
        public Class<GuiContainer> getGuiContainerClass() {
            return GuiContainer.class;
        }

        @Override
        public List<Rectangle> getGuiExtraAreas(GuiContainer gui) {
            if (gui instanceof IArea) {
                return ((IArea) gui).getExtraArea();
            }
            else if (GuiOverlayHandler.isCovered(gui)) {
                return GuiOverlayHandler.getOverlay(gui).getExtraArea();
            }
            else return Collections.emptyList();
        }

        @Override
        public Integer getIngredientUnderMouse(GuiContainer gui, int mouseX, int mouseY) {
            return Mouse.getDWheel();
        }
    }
}
