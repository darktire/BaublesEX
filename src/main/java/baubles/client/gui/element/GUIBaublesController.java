package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;

public class GUIBaublesController extends ElementBase {

    protected final GuiPlayerExpanded parentGui;
    private final int buttonID;
    private boolean pressed;

    public GUIBaublesController(int id, GuiPlayerExpanded parentGui, int x, int y, int buttonID) {
        super(id, x, y, 8, 8, "");
        this.parentGui = parentGui;
        this.buttonID = buttonID;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        pressed = super.mousePressed(mc, mouseX, mouseY);
        return pressed;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (pressed) pressed = false;
        if (buttonID == 2) parentGui.getScroller().visible = !parentGui.getScroller().visible;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHovered(mouseX, mouseY);
            glPush();
            mc.getTextureManager().bindTexture(GuiBaublesBase.BAUBLES_TEX);
            drawTexture(x, y, zLevel, 6 + (pressed ? 8 : 0), buttonID * 8 + 185, 8, 8);
            glPop();
        }
    }
}