package baubles.client.gui;

import baubles.client.gui.element.GUIBaublesButton;
import baubles.common.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEvents {

	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		if (gui instanceof GuiContainer) {
			GuiContainer guiContainer = (GuiContainer) gui;
			if (gui instanceof GuiInventory) {
				if (Config.Gui.baublesButton) {
					event.getButtonList().add(new GUIBaublesButton(55, guiContainer, 64, 9, I18n.format("button.baubles")));
				}
			}
			if (gui instanceof GuiContainerCreative) {
				if (Config.Gui.baublesButton) {
					event.getButtonList().add(new GUIBaublesButton(55, guiContainer, 95, 6, I18n.format("button.baubles")));
				}
			}
		}
	}
}