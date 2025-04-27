package baubles.client.gui;

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
			if (guiContainer instanceof GuiInventory || guiContainer instanceof GuiPlayerExpanded) {
				if (Config.baublesButton) {
					event.getButtonList().add(new GuiBaublesButton(55, guiContainer, 64, 9, 10, 10, I18n.format((guiContainer instanceof GuiInventory) ? "button.baubles" : "button.normal")));
				}
				if (Config.baublesTab) {
					event.getButtonList().add(new GuiBaublesTabButton(60, guiContainer, Config.invPosX, 0));
					event.getButtonList().add(new GuiBaublesTabButton(60, guiContainer, Config.babPosX, 1));
				}
			}
			if (guiContainer instanceof GuiBaublesTab) {
				event.getButtonList().add(new GuiBaublesTabButton(60, guiContainer, Config.invPosX, 0));
				event.getButtonList().add(new GuiBaublesTabButton(60, guiContainer, Config.babPosX, 1));
			}
			if (guiContainer instanceof GuiContainerCreative) {
				boolean visible = ((GuiContainerCreative)guiContainer).getSelectedTabIndex() == 11;
				if (visible & Config.baublesButton) {
					event.getButtonList().add(new GuiBaublesButton(55, guiContainer, 95, 6, 10, 10, I18n.format("button.baubles")));
				}
			}
		}
	}
}
