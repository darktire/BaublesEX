package baubles.client.gui.element;

import baubles.client.gui.GuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ElementScroller extends ElementBase {

    private static final int TRACK_TOP = 16;
    private static final int TRACK_HEIGHT = 142;
    private static final int BAR_HEIGHT = 89;
    private static final int BAR_TRAVEL = TRACK_HEIGHT - BAR_HEIGHT;

    public Rectangle area;

    private int barPos, dragStartY, movement;
    private boolean dragging;

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
            dragging = getHovered(mouseX, mouseY, x + 6, y + TRACK_TOP + barPos, 6, BAR_HEIGHT);
            if (dragging) {
                dragStartY = mouseY;
                movement = 0;
            }
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
            drawTexturedModalRect(x + 6, y + TRACK_TOP + barPos + movement, 0, 167, 6, BAR_HEIGHT - 1);
            drawTexturedModalRect(x + 6, y + TRACK_TOP + BAR_HEIGHT - 1 + barPos + movement, 0, 167 + BAR_HEIGHT - 1, 6, 1);
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
            int row = this.parentGui.getRow();
            if (row < 9) {
                if (this.parentGui.getOffset() != 0) {
                    this.parentGui.moveSlots(-this.parentGui.getOffset());
                }
                this.barPos = 0;
                this.movement = 0;
                return;
            }

            this.movement = Math.max(-this.barPos, Math.min(BAR_TRAVEL - this.barPos, mouseY - this.dragStartY));
            int offset = -Math.round((this.barPos + this.movement) * (row - 8) / (float) BAR_TRAVEL);
            int value = offset - parentGui.getOffset();
            if (value != 0) {
                this.parentGui.moveSlots(value);
            }
        }
    }

    public void moveScrollerBar(int value) {
        int row = this.parentGui.getRow();
        if (row < 9) {
            this.barPos = 0;
            return;
        }

        this.barPos = Math.max(0, Math.min(BAR_TRAVEL, this.barPos - (int) (value * (BAR_TRAVEL / (float) (row - 8)))));
    }

    public void setBarPos(int value) {
        if (this.dragging) return;

        int row = this.parentGui.getRow();
        if (row < 9) {
            this.barPos = 0;
            return;
        }

        this.barPos = Math.max(0, Math.min(BAR_TRAVEL, Math.round(-value * BAR_TRAVEL / (float) (row - 8))));
    }

    public boolean getDragging() {
        return this.dragging;
    }
}
