package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;

public class GUIBaublesController extends ElementBase {

    private final GuiPlayerExpanded parentGui;
    private final int buttonID;
    private boolean pressed;

    public GUIBaublesController(int id, GuiPlayerExpanded parentGui, int x, int y, int buttonID) {
        super(id, x, y, 8, 8, "");
        this.parentGui = parentGui;
        this.buttonID = buttonID;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        this.pressed = super.mousePressed(mc, mouseX, mouseY);
        return this.pressed;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (this.pressed) this.pressed = false;
        if (this.buttonID == 1) {
            this.parentGui.wider = !this.parentGui.wider;
            this.parentGui.offset = 0;
            this.parentGui.updated = false;
        }
        else if (this.buttonID == 2) {
            if (this.parentGui.scroller.visible) {
                this.parentGui.scroller.visible = false;
                this.parentGui.getExtraArea().remove(this.parentGui.scroller.area);
            }
            else {
                this.parentGui.scroller.visible = true;
                this.parentGui.getExtraArea().add(this.parentGui.scroller.area);
            }
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            updateHovered(mouseX, mouseY);
            glPush();
            mc.getTextureManager().bindTexture(GuiBaublesBase.BAUBLES_TEX);
            drawTexturedModalRect(this.x, this.y, 6 + (this.pressed ? 8 : 0), this.buttonID * 8 + 185, 8, 8);
            glPop();
        }
    }
}