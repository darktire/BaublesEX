package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import baubles.client.gui.GuiPlayerExpanded;
import baubles.common.config.cfg.CfgGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;

public class GUIBaublesScroller extends ElementBase {

    protected final GuiPlayerExpanded parentGui;

    private int barPos, dragStartY, movement;
    private boolean dragging;
    private int rest = 142 - 89;

    public GUIBaublesScroller(int id, GuiPlayerExpanded parentGui, int x, int y) {
        super(id, x, y, 18, 166, "");
        this.parentGui = parentGui;
        this.visible = CfgGui.scrollerBar;
        this.barPos = 0;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            if (parentGui.finalLine == 8) dragging = getHovered(mouseX, mouseY, x + 6, y + 16 + barPos, 6, 89);
            if (dragging) dragStartY = mouseY;
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (dragging) {
            dragging = false;
            barPos += movement;
            movement = 0;
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            updateHovered(mouseX, mouseY);
            handleDrag(mouseY);
            glPush();
            mc.getTextureManager().bindTexture(GuiBaublesBase.BAUBLES_TEX);
            drawTexture(x, y, zLevel, 0, 0, 18, 166);
            drawTexture(x + 6, y + 16 + barPos + movement, zLevel, 0, 167, 6, 52 + 36);
            drawTexture(x + 6, y + 16 + 52 + 36 + barPos + movement, zLevel, 0, 167 + 52 + 36, 6, 1);
            glPop();
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {}

    private void handleDrag(int mouseY) {
        if (dragging) {
            movement = mouseY - dragStartY;
            if (barPos + movement < 0) {
                movement = -barPos;
                if (y + 16 + barPos <= mouseY && mouseY < y + 16 + 89 + barPos) dragStartY = mouseY + barPos;
            }
            if (barPos + movement > rest) {
                movement = rest - barPos;
                if (y + 16 + barPos <= mouseY && mouseY < y + 16 + 89 + barPos) dragStartY = mouseY + barPos - rest;
            }
            float pixelPerSlot = (float) rest / (parentGui.baubles.getSlots() - parentGui.finalLine);
            int offset = -Math.round(movement / pixelPerSlot);
            int value = offset - parentGui.offset;
            if (value != 0) {
                this.parentGui.modifyOffset(value);
                if (this.parentGui.needMoveSlots()) this.parentGui.moveSlots();
            }
        }
    }

    public void moveScrollerBar(int value) {
        barPos -= (int) (value * ((float) rest / (parentGui.baubles.getSlots() - parentGui.finalLine)));
        if (barPos < 0) barPos = 0;
        if (barPos > rest) barPos = rest;
    }

    public void setBarPos(int value) {
        barPos = (int) (-value * ((float) rest / (parentGui.baubles.getSlots() - parentGui.finalLine)));
    }
}
