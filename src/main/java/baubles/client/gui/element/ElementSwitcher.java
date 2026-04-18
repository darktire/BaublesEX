package baubles.client.gui.element;

import baubles.client.gui.GuiOverlay;
import baubles.common.network.PacketHandler;
import baubles.common.network.PacketSync;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ElementSwitcher extends ElementBase {
    private boolean on;
    public final int startY;

    public ElementSwitcher(int buttonId, GuiOverlay parentGui, int x, int y) {
        super(buttonId, x, y, 5, 5, "", parentGui);
        this.on = parentGui.baubles.getVisible(buttonId);
        this.startY = y;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean pressed = super.mousePressed(mc, mouseX, mouseY);
            if (pressed) {
                this.on = !this.on;
                this.parentGui.baubles.setVisible(id, this.on);
                PacketHandler.INSTANCE.sendToServer(PacketSync.C2SPack(id, null, this.on ? 1 : 0));
            }
            return pressed;
        }
        return false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.visible = this.parentGui.getGuiTop() < this.y && this.y < this.parentGui.getGuiTop() + 144;
        if (this.visible) {
            updateHovered(mouseX, mouseY);

            glPush();
            mc.getTextureManager().bindTexture(GuiOverlay.BAUBLES_TEX);
            drawTexturedModalRect(this.x, this.y, this.on ? 6 : 14, 209, 5, 5);
            glPop();
        }
    }
}
