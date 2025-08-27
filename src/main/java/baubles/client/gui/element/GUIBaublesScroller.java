package baubles.client.gui.element;

import baubles.client.gui.GuiBaublesBase;
import baubles.client.gui.GuiPlayerExpanded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GUIBaublesScroller extends ElementBase {

    public Rectangle area;

    private final GuiPlayerExpanded parentGui;
    private int barPos, dragStartY, movement;
    private boolean dragging;
    private final int rest = 142 - 89;

    public GUIBaublesScroller(int id, GuiPlayerExpanded parentGui, int x, int y, boolean visible) {
        super(id, x, y, 18, 166, "");
        this.parentGui = parentGui;
        this.visible = visible;
        this.barPos = 0;
        this.area = new Rectangle(this.x, this.y, 18, 166);
        if (visible) this.parentGui.getExtraArea().add( this.area);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            if (parentGui.baublesAmount > 8 * this.parentGui.column) dragging = getHovered(mouseX, mouseY, x + 6, y + 16 + barPos, 6, 89);
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
            drawTexturedModalRect(x, y, 0, 0, 18, 166);
            drawTexturedModalRect(x + 6, y + 16 + barPos + movement, 0, 167, 6, 52 + 36);
            drawTexturedModalRect(x + 6, y + 16 + 52 + 36 + barPos + movement, 0, 167 + 52 + 36, 6, 1);
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
            float pixelPerSlot = (float) rest / (parentGui.baublesAmount - 8 * this.parentGui.column);
            int offset = -Math.round((this.barPos + this.movement) / pixelPerSlot);
            int value = offset - parentGui.offset;
            if (value != 0) {
                this.parentGui.modifyOffset(value);
                if (this.parentGui.needMoveSlots()) this.parentGui.moveSlots();
            }
        }
    }

    public void moveScrollerBar(int value) {
        int i = parentGui.baublesAmount / this.parentGui.column - 8;
        if (parentGui.baublesAmount % this.parentGui.column != 0) i += 1;
        if (i < 1) return;
        barPos -= (int) (value * ((float) rest / i));
        if (barPos < 0) barPos = 0;
        if (barPos > rest) barPos = rest;
    }

    public void setBarPos(int value) {
        int i = parentGui.baublesAmount / this.parentGui.column - 8;
        if (parentGui.baublesAmount % this.parentGui.column != 0) i += 1;
        if (i < 1) return;
        barPos = (int) (-value * ((float) rest / i));
    }

    public boolean getDragging() {
        return this.dragging;
    }
}
