package baubles.client.gui.event;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GuiCloseEvent extends GuiScreenEvent {
    public GuiCloseEvent(GuiScreen gui) {
        super(gui);
    }
}
