package baubles.client.gui.element;

import baubles.client.gui.GuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ElementScroller extends ElementBase {

    public Rectangle area;

    private int barPos, dragStartY, movement;
    private boolean dragging;
    private final int rest = 142 - 89;

    public ElementScroller(int id, GuiOverlay parentGui, int x, int y, boolean visible) {
        super(id, x, y, 18, 166, "", parentGui);
        this.visible = visible;
        this.barPos = 0;
        initArea();
    }

    public void initArea() {
        this.area = new Rectangle(this.x, this.y, 18, 166);
        if (this.visible) this.parentGui.getExtraArea().add(this.area);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            dragging = getHovered(mouseX, mouseY, x + 6, y + 16 + barPos, 6, 89);
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
            mc.getTextureManager().bindTexture(GuiOverlay.BAUBLES_TEX);
            drawTexturedModalRect(x, y, 0, 0, 18, 166);
            drawTexturedModalRect(x + 6, y + 16 + barPos + movement, 0, 167, 6, 52 + 36);
            drawTexturedModalRect(x + 6, y + 16 + 52 + 36 + barPos + movement, 0, 167 + 52 + 36, 6, 1);
            glPop();
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {}

    public void handleWider() {
        this.x = this.parentGui.getGuiLeft() - 30 - 18 * this.parentGui.getCol();
        this.barPos = 0;
        initArea();
    }

    public void switchVisible() {
        if (this.visible) {
            this.visible = false;
            this.parentGui.getExtraArea().remove(this.area);
        }
        else {
            this.visible = true;
            this.parentGui.getExtraArea().add(this.area);
        }
    }

    private void handleDrag(int mouseY) {
        if (dragging) {
            if (this.parentGui.getRow() < 9) {
                if (this.parentGui.getOffset() != 0) {
                    this.parentGui.moveSlots(0);
                    this.barPos = 0;
                }
                return;
            }
            movement = mouseY - dragStartY;
            if (barPos + movement < 0) {
                movement = -barPos;
                if (y + 16 + barPos <= mouseY && mouseY < y + 16 + 89 + barPos) dragStartY = mouseY + barPos;
            }
            if (barPos + movement > rest) {
                movement = rest - barPos;
                if (y + 16 + barPos <= mouseY && mouseY < y + 16 + 89 + barPos) dragStartY = mouseY + barPos - rest;
            }
            float pixelPerSlot = (float) rest / (parentGui.baublesAmount - 8 * this.parentGui.getCol());
            int offset = -Math.round((this.barPos + this.movement) / pixelPerSlot);
            int value = offset - parentGui.getOffset();
            if (value != 0) {
                this.parentGui.moveSlots(value);
            }
        }
    }

    public void moveScrollerBar(int value) {
        int i = this.parentGui.getRow();
        if (i < 9) return;
        barPos -= (int) (value * ((float) rest / (i - 8)));
        if (barPos < 0) barPos = 0;
        if (barPos > rest) barPos = rest;
    }

    public void setBarPos(int value) {
        int i = this.parentGui.getRow();
        if (i < 9) return;
        barPos = (int) (-value * ((float) rest / (i - 8)));
    }

    public boolean getDragging() {
        return this.dragging;
    }
}
