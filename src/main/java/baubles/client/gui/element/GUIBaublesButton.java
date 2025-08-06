package baubles.client.gui.element;

import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;
import baubles.common.network.PacketOpenNormalInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;

public class GUIBaublesButton extends ElementBase {

    private final GuiContainer parentGui;

    public GUIBaublesButton(int buttonId, GuiContainer parentGui, int x, int y, String buttonText) {
        super(buttonId, parentGui.getGuiLeft() + x, parentGui.getGuiTop() + y, 10, 10, buttonText);
        this.parentGui = parentGui;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (this.hovered && this.enabled) {
            if (this.parentGui instanceof GuiPlayerExpanded) {
                ((GuiPlayerExpanded) this.parentGui).displayNormalInventory();
                PacketHandler.INSTANCE.sendToServer(new PacketOpenNormalInventory());
            } else{
                PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory());
            }
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            if (this.parentGui instanceof GuiContainerCreative && ((GuiContainerCreative) this.parentGui).getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex()) return;
            updateHovered(mouseX, mouseY);

            glPush();
            mc.getTextureManager().bindTexture(GuiPlayerExpanded.background);
            if (this.hovered) {
                drawTexture(x, y, zLevel, 200, 48, 10, 10);
                FontRenderer fontrenderer = mc.fontRenderer;
                drawCenteredString(fontrenderer, I18n.format(this.displayString), x + 5, this.y + this.height, 0xffffff);
            }
            else {
                drawTexture(x, y, zLevel, 210, 48, 10, 10);
            }
            glPop();
        }
    }
}