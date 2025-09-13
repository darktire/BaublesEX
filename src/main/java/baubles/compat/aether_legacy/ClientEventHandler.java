package baubles.compat.aether_legacy;

import baubles.common.config.Config;
import com.gildedgames.the_aether.client.gui.button.GuiAccessoryButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ClientEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void blockAdding(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!Config.Gui.aetherButton) {
            GuiScreen gui = event.getGui();
            if (gui instanceof GuiContainerCreative || gui instanceof GuiInventory) {
                List<GuiButton> buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, gui, "field_146292_n");;
                buttonList.removeIf(GuiAccessoryButton.class::isInstance);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMouse(GuiScreenEvent.MouseInputEvent.Post event) {
        if (!Config.Gui.aetherButton) {
            GuiScreen gui = event.getGui();
            if (gui instanceof GuiContainerCreative && ((GuiContainerCreative) gui).getSelectedTabIndex() == CreativeTabs.INVENTORY.getIndex()) {
                List<GuiButton> buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, gui, "field_146292_n");;
                buttonList.removeIf(GuiAccessoryButton.class::isInstance);
            }
        }
    }
}
