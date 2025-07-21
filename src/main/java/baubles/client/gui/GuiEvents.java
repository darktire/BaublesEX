package baubles.client.gui;

import baubles.client.gui.botton.GuiBaublesButton;
import baubles.common.config.cfg.CfgGui;
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
			if (gui instanceof GuiInventory || gui instanceof GuiPlayerExpanded) {
				if (CfgGui.baublesButton) {
					event.getButtonList().add(new GuiBaublesButton(55, guiContainer, 64, 9, 10, 10, I18n.format((guiContainer instanceof GuiInventory) ? "button.baubles" : "button.normal")));
				}
			}
			if (gui instanceof GuiContainerCreative) {
				if (CfgGui.baublesButton) {
					event.getButtonList().add(new GuiBaublesButton(55, guiContainer, 95, 6, 10, 10, I18n.format("button.baubles")));
				}
			}
		}
	}
}